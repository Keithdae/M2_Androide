# -*- coding: utf-8 -*-
"""
Created on Tue Jul 12 23:25:48 2016

@author: nicolas
"""

import pulp
from problem import Problem

###############################################################################
# The Assignment LP
###############################################################################



def assignmentLP(p,verbose=False):
    
    agents = list(range(1,p.n)) # agent 0 is the auctioneer
    resources = list(range(0,p.m))
    
    
    # building the pulp model    
    
    assignLP = pulp.LpProblem("Assignment LP",pulp.LpMaximize)

    # creating the variables

    x = pulp.LpVariable.dicts("assignement", (agents, resources),cat = pulp.LpBinary)
    k = pulp.LpVariable("bound", lowBound = 0)


    # objective function: maximize k
    
    assignLP += k    
    
    # each resource assigned to one agent
    for j in resources: 
        assignLP += sum([x[i][j] for i in agents])==1
        
    # utility of each agent must be higher than k
    for i in agents:
        assignLP += sum([p.agent[i].u["r"+str(j)]*x[i][j] for j in resources])>=k

        
    # solving and printing

    assignLP.solve()
    if verbose:
        for i in agents:
            for j in resources:
                if (x[i][j].varValue == 1):
                    print ("agent ", i, " gets resource r"+str(j))
                
    return k.varValue
    
        


###############################################################################
# The Envy-Minimizing LP
###############################################################################

def envyminimizingLP(p,verbose=False):
    
    agents = list(range(1,p.n)) # agent 0 is the auctioneer
    resources = list(range(0,p.m))
    
    
    envyLP = pulp.LpProblem("Assignment LP",pulp.LpMinimize)
    
    # creating the variables

    x = pulp.LpVariable.dicts("assignement", (agents, resources),cat = pulp.LpBinary)
    e = pulp.LpVariable("bound", lowBound = 0)
    
    # objective function: maximize k
    
    envyLP += e
    
    # each resource assigned to one agent
    for j in resources: 
        envyLP += sum([x[i][j] for i in agents])==1
        
    # utility of each agent must be higher than k
    for i in agents:
        for j in agents: 
            if i != j:
                envyLP += sum([p.agent[i].u["r"+str(k)]*x[j][k] for k in resources]) - sum([p.agent[i].u["r"+str(k)]*x[i][k] for k in resources]) <=e
        
    # solving and printing

    envyLP.solve()
    if verbose:
        for i in agents:
            for j in resources:
                if (x[i][j].varValue == 1):
                    print ("agent ", i, " gets resource r"+str(j))
    
    return e.varValue



