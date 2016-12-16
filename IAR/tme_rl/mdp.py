import numpy as np;
import matplotlib.pyplot as plt
import matplotlib.patches as mpatches

##########-ACTIONS DEFINITION-###################
N = 0
S = 1
E = 2
W = 3
NoOp = 4

class mdp():

######################-MDP DEFINITION-###########

    def __init__(self):
        self.nX = 16 # number of states
        self.nU = 5 # number of actions

        self.P0 = np.array([1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0])

        self.P = np.empty((self.nX,self.nU,self.nX)) # Transition function (P(x_t,u_t,X_t+1))
        self.P[:,N,:]=  np.array([
            [1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0],
            [1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0],
            [0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0],
            [0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0],
            [0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0],
            [0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0],
            [0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0],
            [0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0],
            [0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0],
            [0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0],
            [0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0],
            [0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0],
            [0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0],
            [0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0],
            [0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0],
            [0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0]])

        self.P[:,S,:]= np.array([
            [0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0],
            [0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0],
            [0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0],
            [0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0],
            [0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0],
            [0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0],
            [0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0],
            [0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0],
            [0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0],
            [0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0],
            [0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0],
            [0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0],
            [0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0],
            [0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0],
            [0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0],
            [0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0]])
 
        self.P[:,E,:]= np.array([
            [0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0],
            [0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0],
            [0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0],
            [0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0],
            [0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0],
            [0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0],
            [0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0],
            [0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0],
            [0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0],
            [0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0],
            [0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0],
            [0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0],
            [0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0],
            [0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0],
            [0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0],
            [0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0]])

        self.P[:,W,:]= np.array([
            [1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0],
            [0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0],
            [0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0],
            [0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0],
            [1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0],
            [0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0],
            [0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0],
            [0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0],
            [0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0],
            [0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0],
            [0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0],
            [0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0],
            [0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0],
            [0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0],
            [0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0],
            [0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0]])

        self.P[:,NoOp,:]=  np.eye(self.nX)

        self.r = np.zeros((self.nX, self.nU));
        self.r[self.nX - 1, NoOp] = 1;
        
        # 2.1 Value Iteration Question 3
        self.r[1,NoOp] = 0.9;
        # Le robot choisit de se rendre vers l'etat le plus proche offrant une recompense (etat 2 et 16).
        # Gamma est le parametre responsable de ce comportement.
        # Avec une valeur de gamma tres proche de 1, par exemple 0.99, le robot ignorera la case 2
        
        # gamma = [0;1], 0 -> cherche recompense immediate / 1 -> cherche recompense long terme
        self.gamma = 0.7;
        
        # 2.1 Value Iteration Question 4 : transitions stochastiques
        # On modelise un obstacle entre les etats 2,6,10 et ceux en-dessous 3,7,11.
        # Resultats :
        # Un seul objectif :
        # Le robot evite les transitions stochastiques que nous avons definies alors qu'il les utilisait auparavant.
        
        # probaObstacle est la probabilite de ne pas franchir l'obstacle et rester sur place
        probaObstacle = 0.5
        
        self.P[1,S,2] = 1 - probaObstacle
        self.P[5,S,6] = 1 - probaObstacle
        self.P[9,S,10] = 1 - probaObstacle
        self.P[1,S,1] = probaObstacle
        self.P[5,S,5] = probaObstacle
        self.P[9,S,9] = probaObstacle
        
        self.P[2,N,1] = 1 - probaObstacle
        self.P[6,N,5] = 1 - probaObstacle
        self.P[10,N,9] = 1 - probaObstacle
        self.P[2,N,2] = probaObstacle
        self.P[6,N,6] = probaObstacle
        self.P[10,N,10] = probaObstacle
        
        
###################-DYNAMIC PROGRAMMING-###############


    def VI(self):
        Q = np.zeros((self.nX,self.nU))
        pol = N*np.ones((self.nX,1))
        quitt = False
        iterr = 0
        
        while quitt==False:
            iterr += 1
            print iterr
            Qold = Q.copy()
            Qmax = Qold.max(axis=1)
            
            for i in range(self.nX):
                for j in range(self.nU):
                    Q[i,j] = self.r[i,j] + self.gamma * np.sum(self.P[i,j,:] * Qmax)

            Qmax = Q.max(axis=1)
            pol =  np.argmax(Q,axis=1)
            if (np.linalg.norm(Q-Qold))==0 :
                quitt = True
        return [Q,pol]

    def PI(self):
        Q = np.empty((self.nX,self.nU))
        pol = N*np.ones(self.nX,dtype=np.int16)
        I = np.eye((self.nX))
        R = np.zeros((self.nX,1))
        P = np.zeros((self.nX,self.nX))
        quitt = False
        iterr = 0

        while quitt==False:
            iterr += 1
            print iterr
            
            for i in range(self.nX):
                R[i]= self.r[i,pol[i]]
                for j in range(self.nX):
                    P[i,j] = self.P[i,pol[i],j]
            
            V = np.dot(np.linalg.inv(I-self.gamma*P) ,R)
            

            for i in range(self.nX):
                for j in range(self.nU):
                    Q[i,j] = self.r[i,j] + self.gamma * np.sum(self.P[i,j,:] * V.reshape((self.nX)))
            
            pol_old = pol.copy()
            Qmax = Q.max(axis=1)
            pol =  np.argmax(Q,axis=1)
            
            
            if np.array_equal(pol,pol_old) :
                quitt = True

        return [Q,pol]



##############-TD-LEARNING-########################
    def discreteProb(self,p):
        # Draw a random number using probability table p (column vector)
        # Suppose probabilities p=[p(1) ... p(n)] for the values [1:n] are given, sum(p)=1 and the components p(j) are nonnegative. To generate a random sample of size m from this distribution imagine that the interval (0,1) is divided into intervals with the lengths p(1),...,p(n). Generate a uniform number rand, if this number falls in the jth interval give the discrete distribution the value j. Repeat m times.
        r = np.random.random()
        cumprob=np.hstack((np.zeros(1),p.cumsum()))
        sample = -1
        for j in range(p.size):
            if (r>cumprob[j]) & (r<=cumprob[j+1]):
                sample = j
                break
        return sample

    def MDPStep(self,x,u):
	# This function executes a step on the MDP M given current state x and action u.
        # It returns a next state y and a reward r
        y = self.discreteProb(self.P[x,u,:]) # y should be sampled according to the discrete distribution self.P[x,u,:]
        r = self.r[x,u] # r should be the reward of the transition
        return [y,r]


    def TD(self,pol):
        V = np.zeros((self.nX,1))
        nbIter = 100000
        alpha = 0.1
        for i in range(nbIter):
            x = np.floor(self.nX*np.random.random())
            [y,r] = self.MDPStep(x,pol[x])
            V[x] = V[x] + alpha * (r + self.gamma * V[y] - V[x])
        return V



##################-Q-LEARNING-############
    
    def softmax(self,Q,x,tau):
        # Returns a soft-max probability distribution over actions
        # Inputs :
        # - Q : a Q-function reprensented as a nX times nU matrix
        # - x : the state for which we want the soft-max distribution
        # - tau : temperature parameter of the soft-max distribution
        # Output :
        # - p : probabilty of each action according to the soft-max distribution
        #(column vector of length nU)
        p = np.zeros((self.nU))
        denominateur = np.sum(np.exp(Q[x,:]/tau))
        for i in range(self.nU):
            numerateur = np.exp(Q[x,i]/tau)
            p[i] = numerateur / denominateur
        return p

    def QLearning(self,tau):
        # This function computes the optimal state-value function and the corresponding policy using Q-Learning

        # Initialize the state-action value function
        Q = np.zeros((self.nX,self.nU))

        # Run learning cycle
        nbIter = 100000
        alpha = 0.01
        for i in range(nbIter):
            # Draw a random state
            x = np.floor(self.nX*np.random.random())
            
            # Draw an action using a soft-max policy
            u = self.discreteProb(self.softmax(Q,x,tau))

            # Perform a step of the MDP
            [y,r] = self.MDPStep(x,u)
            
            # Update the state-action value function with Q-Learning
            Q[x,u] = Q[x,u] + alpha * (r + self.gamma * np.max(Q[y,:]) - Q[x,u])

        # Compute the corresponding policy
        Qmax = Q.max(axis=1)
        pol =  np.argmax(Q,axis=1)
        return [Qmax,pol]

###################-RTDP-#################

    def RTDP(self):
        x = self.discreteProb(self.P0)
        Q = np.zeros((self.nX,self.nU))
        hatP = np.ones((self.nX,self.nU,self.nX))/self.nX
        N = np.ones((self.nX,self.nU))

        I = np.eye((self.nX))

        nbIter = 10000

        for iterr in range(nbIter):
            u = np.floor(self.nU*np.random.random())
            [y,r] = self.MDPStep(x,u)
            hatP[x,u,:] = (1 - 1.0/(N[x,u])) * hatP[x,u,:]
            hatP[x,u,y] += 1.0/N[x,u]
            Qmax = Q.max(axis=1)
            Q[x,u] = r + self.gamma * np.sum(hatP[x,u,:] * Qmax)
            N[x,u] = N[x,u]+1
            x=y
        

        Qmax =Q.max(axis=1) 
        pol =  np.argmax(Q,axis=1)

        return [Qmax,pol]



###################-PLOT-#################

def statePos(i):
	return (0.05 + (((i-1) / 4)/10.0), 0.35 - ((i-1) % 4)/10.0)

def statePos_text(i):
	return (statePos(i)[0] - 0.025, statePos(i)[1] + 0.01)
def arrow(i,j):
	dx = 0.9 *(statePos(j)[0] - statePos(i)[0])
	dy = 0.9 * (statePos(j)[1] - statePos(i)[1])
	x0 = statePos(i)[0] + 1.7*dx/18.0
	y0 = statePos(i)[1] + 1.7*dy/18.0
	return (x0, y0, dx, dy)

def plotPolicy(m,pol):
	fig, ax = plt.subplots()
	plt.vlines(0,0.0,0.4,lw=3.5)
	plt.vlines(0.4,0.0,0.4,lw=3.5)
	plt.hlines(0,0.0,0.4,lw=3.5)
	plt.hlines(0.4,0.0,0.4,lw=3.5)
	for i in range(1,17):
		print m.P[i-1,pol[i-1,0],:]
		arr = arrow(i,1+m.P[i-1,pol[i-1,0],:].argmax())
		if( arr[2] != 0.0 or arr[3] != 0.0):
			plt.arrow(*arr, length_includes_head = True, width=0.005)		
		else:
			circle = mpatches.Circle(statePos(i), 0.01, ec="none")
			ax.add_patch(circle)
	for i in range(1,17):
		plt.annotate(i, xy=(0,0), xytext=statePos_text(i))

	plt.xticks([i/10.0 for i in range(0,5)], visible = False )
	plt.yticks([i/10.0 for i in range(0,5)], visible = False )
	plt.grid(which='both', ls='--')
	plt.axis('equal')
	plt.axis([0.0,0.4,0.0,0.4])
	plt.show(fig)

