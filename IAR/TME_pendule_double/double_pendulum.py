from __future__ import division
from numpy import sin, cos, pi, array
import numpy as np
import matplotlib.pyplot as plt
import scipy.integrate as integrate
import matplotlib.animation as animation

g =  9.8 # gravite (m/s^2)
l = 1.0 # longueur des segments (m)
m = 1.0 # masse des segments (kg)

# t est une liste d'instants (separes par dt)
dt = 30e-3
t = np.arange(0.0, 15, dt)

def torque(state, t):
    tor = np.zeros(2)
    d = np.zeros_like(state)
    k = 50
    q = 10
    tor[0] = -k*(state[0]-(pi/4)) 
    tor[1] = -k*(state[2]-(pi/4)) 

    d[0] = (6/(m*l*l))*( (2*state[1] - 3 * cos(state[0]-state[2])*state[3]) / (16-9*cos(state[0]-state[2])*cos(state[0]-state[2])) )
    d[2] = (6/(m*l*l))*( (8*state[3] - 3 * cos(state[0]-state[2])*state[1]) / (16-9*cos(state[0]-state[2])*cos(state[0]-state[2])) )    
    
    tor[0] = -k*(state[0]-(-pi)) - q * state[1] - (-1/2) * m * l * l * (d[0]*d[2]*sin(state[0]-state[2]) + 3 *(g/l)* sin(state[0]))
    tor[1] = -k*(state[2]-(-pi)) - q * state[3] - (-1/2) * m * l * l * (-d[0]*d[2]*sin(state[0]-state[2]) + (g/l)* sin(state[2]))
    return tor

def derivs(state, t):
    d = np.zeros_like(state)
    torq = torque(state, t)
    d[0] = (6/(m*l*l))*( (2*state[1] - 3 * cos(state[0]-state[2])*state[3]) / (16-9*cos(state[0]-state[2])*cos(state[0]-state[2])) )
    d[2] = (6/(m*l*l))*( (8*state[3] - 3 * cos(state[0]-state[2])*state[1]) / (16-9*cos(state[0]-state[2])*cos(state[0]-state[2])) )
    d[1] = (-1/2) * m * l * l * (d[0]*d[2]*sin(state[0]-state[2]) + 3 *(g/l)* sin(state[0])) + torq[0]
    d[3] = (-1/2) * m * l * l * (-d[0]*d[2]*sin(state[0]-state[2]) + (g/l)* sin(state[2])) + torq[1]
    return d

# th1 et th2 sont les angles initiaux (degres)
# p1 et p2 sont les moments generalises
th1 = 115.0
p1 = 0.0
th2 = 45.0
p2 = 0.0

# etat initial
state = np.array([th1, p1, th2, p2])*pi/180.

# integration de la dynamique
y = integrate.odeint(derivs, state, t)

x1 = l*sin(y[:,0])
y1 = -l*cos(y[:,0])

x2 = l*sin(y[:,2]) + x1
y2 = -l*cos(y[:,2]) + y1

fig = plt.figure()
ax = fig.add_subplot(111, autoscale_on=False, xlim=(-2, 2), ylim=(-2, 2))
ax.grid()

line, = ax.plot([], [], 'o-', lw=2)
time_template = 'time = %.1fs'
time_text = ax.text(0.05, 0.9, '', transform=ax.transAxes)

def init():
    line.set_data([], [])
    time_text.set_text('')
    return line, time_text

def animate(i):
    thisx = [0, x1[i], x2[i]]
    thisy = [0, y1[i], y2[i]]

    line.set_data(thisx, thisy)
    time_text.set_text(time_template%(i*dt))
    return line, time_text

ani = animation.FuncAnimation(fig, animate, frames=len(y),
    interval=dt*1e3, init_func=init)

#ani.save('double_pendulum.mp4', fps=15)
plt.axis('equal')
plt.axis([-3.0,3.0,-3.0,3.0])
plt.show()

z = np.ones_like(y[:,0])*45*pi/180.0

xvalues = np.arange(len(y))*dt

plt.plot(xvalues, y[:,0])
plt.plot(xvalues, y[:,2])
plt.plot(xvalues, z)
plt.show()

