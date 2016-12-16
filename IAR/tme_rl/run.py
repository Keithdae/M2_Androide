from mdp import *


m = mdp()


print '-------------VI---------------'
[Q,pol] = m.VI()

print Q

print pol

plotPolicy(m,np.transpose(np.array([pol])))

print '-------------PI---------------'
[Q,pol] = m.PI()

print '-------------TD-learning---------------'
V = m.TD(pol)

print '-------------Q-learning---------------'
#[V,pol] = m.QLearning(0.5)
[V,pol] = m.RTDP()


print V

print pol

plotPolicy(m,np.transpose(np.array([pol])))
