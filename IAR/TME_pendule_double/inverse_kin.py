from __future__ import division
from numpy import sin, cos, pi, array
import numpy as np
import matplotlib.pyplot as plt
import scipy.integrate as integrate
import matplotlib.animation as animation

def p_target(i):
    #return np.array([0.8*cos(0.6 + 0.005*i), 0.8*sin(0.6 + 0.005*i)])
    #return np.array([0.5,-0.5])
    #return np.array([0.0005*i,-0.0005*i])
    return np.array([0.8*(sin(0.005*i)/(1+cos(0.005*i)*cos(0.005*i))),
                     0.8*((sin(0.005*i)*cos(0.005*i))/(1+cos(0.005*i)*cos(0.005*i)))])

N = 15
l = 1.0/N
delta = 0.02
ITERATIONS = 3000

q = np.zeros(N)
q0 = np.zeros(N)
q.fill(0.2)

print q

midpoints_x = np.zeros((N+1,ITERATIONS))
midpoints_y = np.zeros((N+1,ITERATIONS))

p = np.zeros(2)

print p

for count in range(ITERATIONS):

    midpoints_x[0, count] = 0.0
    midpoints_y[0, count] = 0.0
    
    angleAbsolu = 0
    for i in range(1, N+1):  
      angleAbsolu += q[i-1]
      midpoints_x[i, count] = midpoints_x[i-1, count] + l * cos(angleAbsolu)
      midpoints_y[i, count] = midpoints_y[i-1, count] + l * sin(angleAbsolu)
      # midpoints_x[i, count] = ...
      # midpoints_y[i, count] = ...    
      
    p[0] = midpoints_x[N,count]
    p[1] = midpoints_y[N,count]  
    
    v_des = p_target(count) - p

    J = np.zeros((2,N))
#    angleAbsolu = q[0]
#    J[0,0] = - l * sin(angleAbsolu)
#    J[1,0] = l * cos(angleAbsolu)
#    for i in range(1,N):
#        angleAbsolu += q[i]
#        J[0,i] = -l * sin(angleAbsolu)
#        J[1,i] = l * cos(angleAbsolu)
    angleTotal = sum(q)
    i = N-2
    J[0,N-1] = -l * sin(angleTotal)
    J[1,N-1] = l * cos(angleTotal)
    while i >= 0:
        J[0,i] = J[0,i+1] - l * sin(angleTotal)
        J[1,i] = J[1,i+1] + l * cos(angleTotal)
        angleTotal -= q[i]
        i-=1

    #q_prime = np.dot(np.linalg.pinv(J),v_des) 
    q_prime = np.dot(np.linalg.pinv(J),v_des) + np.dot(np.identity(N) - np.dot(np.linalg.pinv(J), J), (q0-q))
    q = q + delta/(np.linalg.norm(q_prime) + 1e-6) * q_prime    


fig = plt.figure()
ax = fig.add_subplot(111, autoscale_on=False, xlim=(-1, 1), ylim=(-1, 1))
ax.grid()

line, = ax.plot([], [], 'o-', lw=2)
pt, = ax.plot([], [], 'o', c='black')
time_template = 'time = %.1fs'
time_text = ax.text(0.05, 0.9, '', transform=ax.transAxes)

def init():
    line.set_data([], [])
    pt.set_data([], [])
    time_text.set_text('')
    return line, pt, time_text

def animate(i):
    thisx = midpoints_x[:,i] 
    thisy = midpoints_y[:,i] 
    
    line.set_data(thisx, thisy)
    pt.set_data([p_target(i)[0]],[p_target(i)[1]])
    time_text.set_text(time_template%(i*0.05))
    return line, pt, time_text

ani = animation.FuncAnimation(fig, animate, frames=ITERATIONS,
    interval=5, init_func=init)

plt.axis('equal')
plt.axis([-1.2,1.2,-1.0,1.0])
plt.show()














