#version 330 core

out vec4 fragColor;

uniform vec2 iResolution;
uniform float iTime;
vec2 fragCoord = vec2(gl_FragCoord.xy);

//----

#define GRASS_ENV 0.2
#define FREQ 340.
#define PI 3.1415926535

float hash11(float p)
{
    vec2 p2 = fract(vec2(p * 5.3983, p * 5.4427));
    p2 += dot(p2.yx, p2.xy + vec2(21.5351, 14.3137));
    return fract(p2.x * p2.y * 95.4337)*0.5+0.5;
}

vec3 hash33(float n) { return fract(sin(vec3(n,n+7.3,n+13.7))*1313.54531); }
vec3 noise(float x)
{
    float p=floor(x);
    float f=fract(x);
    f=f*f*(3.0-2.0*f);
    return mix(hash33(p+0.0), hash33(p+1.0), f);
}

float noise(vec2 x)
{
    vec2 p=floor(x);
    vec2 f=fract(x);
    f=f*f*(3.0-2.0*f);
    float n=p.x + p.y*57.0;
    return mix(mix(hash11(n+0.0), hash11(n+1.0),f.x),
               mix(hash11(n+57.0), hash11(n+58.0),f.x),f.y);
}

float fbm(vec2 p)
{
    float f=0.0;
    f+=.5*noise(p); p=p*2.01;
    f+=.25*noise(p); p=p*2.1;
    f+=.125*noise(p); p=p*2.03;
    return f;
}

float terrain(vec2 p) 
{
    float w=0.;
    float s=1.;
    p.x*=20.;
    w+=sin(p.x*.3521)*4.;
    for (int i=0; i<5; i++) 
    {
        p.x*=1.53562;
        p.x+=7.56248;
        w+=sin(p.x)*s;      
        s*=.5;
    }
    w=w*.5+.5;
    return smoothstep(0.,0.05,p.y-w*.01-0.02);
}

vec3 renderTerrian(vec3 ro, vec3 rd, vec3 col)
{
    col=mix(col,vec3(0.05,0.1,0.2)*2.7, 1.-terrain(rd.xy));
	
    return col;
}

float mapTerrian(vec3 p)
{
    vec2 q=p.xz*1.1-vec2(2.3,0.);
    float f=0.0;
    f+=.56*noise(q); q=q*2.01;
    f+=.25*noise(q); q=q*2.1;
    //f+=.125*noise(q); q=q*2.03;
    return f-0.3;
}

float traceTerrian(vec3 ro, vec3 rd)
{
    float t=0.01,d,h;
    float tPrev=t;
    vec3 p;
    float grassMint=100.;
    for(int i=0;i<200;++i)
    {
        p=ro+t*rd;
        h=mapTerrian(p);    
        d=p.y-h;
        
        if(p.y-h-GRASS_ENV < 0.001 && grassMint>99.)
        {
			grassMint=tPrev;   
            break;
        }
       
        if(t>=100.0) { break;}
        
        tPrev=t;
        t+=d;
    }
 
    return grassMint;
}

float blades[8];
    
void rotate(float v)
{
    blades[7]=blades[6]; blades[6]=blades[5]; blades[5]=blades[4];
    blades[4]=blades[3]; blades[3]=blades[2]; blades[2]=blades[1];
    blades[1]=blades[0]; blades[0]=v;
}

bool useAA=false;
float oldD=0.;

float shear(vec3 p, float t)
{
    return sin(t+p.x*3.+p.z*(2.))*.3*(p.y)*(1.-smoothstep(-1.5, 2., p.z));
}

float grassNoise(vec2 p)
{
    vec2 l=floor(p);
    vec2 f=fract(p);
    f=f*f*(3.-2.*f);
    float n=l.x+l.y*57.;
    float d=mix(mix(hash11(n+0.0), hash11(n+1.0),f.x),mix(hash11(n+57.0), hash11(n+58.0),f.x),f.y);
    return d;
}

float mapGrass(vec3 p)
{
    vec3 q=p;
    // Shear
    p.xy=vec2(p.x+shear(p,0.)+.02*shear(p,iTime*2.), p.y);
    float d=grassNoise(p.xz*FREQ);
    d*=mix(1.,grassNoise(p.xz*30.),0.6);
    
    #if 1
    if(useAA)
    {
        rotate(d);
        d = (blades[0]+blades[1]+blades[2]+blades[3]
            +blades[4]+blades[5]+blades[6]+blades[7])*0.125;
    }
    #else
    if(useAA&&oldD>0.)
    {
        d=(oldD+d)*.5;
    }
    oldD=d;
    #endif
    
    d*=GRASS_ENV;
    return d;
}

float occlusion(vec3 p)
{
    // 1 cell backwards
 	float w=1./FREQ;
    // check the difference of height
    float d=p.y-mapGrass(p-vec3(0,0,w))-mapTerrian(p);
    // normalize
    return clamp(d/GRASS_ENV, 0.0, 1.0);
}

float bisect(vec3 ro, vec3 rd, float near, float far)
{
    float mid=0.;
    vec3 p=ro+near*rd;
    float sgn=sign(p.y-mapGrass(p)-mapTerrian(p));
    for (int i=0; i<6; i++)
    { 
        mid=(near+far)*.5;
        p=ro+mid*rd;
        float d=p.y-mapGrass(p)-mapTerrian(p);
        if(abs(d)<0.001)break;
        d*sgn<0. ? far=mid : near=mid;
    }
    return (near+far)*.5;
}

vec4 renderGrass(vec3 ro, vec3 rd, float mint)
{
	float t=mint;//((ro.y-GRASS_ENV)/abs(rd.y));

    vec3 p=ro+t*rd;
    float d=p.y-mapGrass(p)-mapTerrian(p);
	float sgn=sign(d);
    float told=0.;
	bool doBisect=false;

    useAA=true;
    
    for(int i=0;i<500;++i)
    {
        d=p.y-mapGrass(p)-mapTerrian(p);
        if (sign(d)!=sgn)
        {
            doBisect=true;
            break;
        }
        
        if(d<0.003||t>=50.0)
        {
            break;
        }
        
        told=t;
        
	    d=max(1e-4, 0.04*d*exp(t*.2));
        p+=d*rd;
        t+=d;
    }
    if (doBisect)t=bisect(ro,rd,told,t);
    
    useAA=false;
    vec3 col=vec3(0.1,0.5,1.)*3.5;
    col=renderTerrian(ro,rd,col);

    if(t<50.)
    {
        vec3 ld0=normalize(vec3(5., 5.0, 0.));
        col=vec3(0.1,0.2,0.05)*10.;
        col=mix(col,vec3(0.12,0.1,0.05)*10.,fbm(p.xz*2.));       
        col*=smoothstep(GRASS_ENV*0.45,GRASS_ENV*2.,p.y-mapTerrian(p));
        col*=pow(smoothstep(0.,1.,fbm(p.xz)),2.);
 
        col*=.7*smoothstep(0.3,0.5,p.y)+.3;
 
        col*=.8*occlusion(p)+.2;
        
        col*=20.;
    	col=mix(col,vec3(0.05,0.1,0.2)*2.7, 1.0-exp(-0.01*t*t));
    }
    
    return vec4(col,t);
}

vec3 tonemap(vec3 x) 
{
    const float a=2.51, b=0.03, c=2.43, d=0.59, e=0.14;
    return (x*(a*x+b))/(x*(c*x+d)+e);
}

vec3 firecolor(float f)
{
	f=f*f*(3.-2.*f);
    return min(vec3(f+.8, f*f*1.4+.1, f*f*f*.7)*f, 1.);
}

void main()
{
	vec2 q = fragCoord.xy / iResolution.xy;
    vec2 p = -1.0 + 2.0 * q;
    p.x *= iResolution.x/iResolution.y;
	if(abs(p.y)-0.85>0.) { fragColor.xyz=vec3(0); return; }
    
    vec3 lookat=vec3(0.0, 0., 0.0);
	vec3 ro=vec3(0., .8,-2.5);
    ro.xy+=vec2(0.04,0.01)*noise(iTime*0.5).xy;
    
    vec3 forward=normalize(lookat-ro);
    vec3 right=normalize(cross(forward, vec3(0.0, 1.0, 0.0)));
    vec3 up=normalize(cross(right, forward));
    
    vec3 rd=normalize(p.x*right + p.y*up + 1.6*forward);
    
    vec3 col=vec3(0.1,0.5,1.)*0.1;
	
    float grassMint=traceTerrian(ro,rd);
	vec4 res=renderGrass(ro,rd,grassMint);
    col=res.xyz;
    col+=firecolor(0.5)*pow(clamp(dot(normalize(vec3(.0, 1., 2.)),rd), 0.0, 1.0),10.);
    col=tonemap(col);
    col=pow(clamp(col,0.0,1.0),vec3(0.45));
    col=col*0.6+0.4*col*col*(3.0-2.0*col);
    //col=pow(col,vec3(0.85,0.9,1.));
    col*=pow(16.0*q.x*q.y*(1.0-q.x)*(1.0-q.y), 0.2);
    fragColor.xyz=col;
}