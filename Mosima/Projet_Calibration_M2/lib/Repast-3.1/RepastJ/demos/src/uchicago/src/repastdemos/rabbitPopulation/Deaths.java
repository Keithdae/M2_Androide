/*$$
 * Copyright (c) 2004, Repast Organization for Architecture and Design (ROAD)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with 
 * or without modification, are permitted provided that the following 
 * conditions are met:
 *
 *	 Redistributions of source code must retain the above copyright notice,
 *	 this list of conditions and the following disclaimer.
 *
 *	 Redistributions in binary form must reproduce the above copyright notice,
 *	 this list of conditions and the following disclaimer in the documentation
 *	 and/or other materials provided with the distribution.
 *
 * Neither the name of the ROAD nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE TRUSTEES OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *$$*/
package uchicago.src.repastdemos.rabbitPopulation;

import uchicago.src.sim.engine.CustomProbeable;
import uchicago.src.sim.gui.Named;
import uchicago.src.sim.gui.RoundRectNetworkItem;
import uchicago.src.sim.math.CEquation;
import uchicago.src.sim.math.CEquationFactory;
import uchicago.src.sim.network.DefaultDrawableNode;

/**
 * Calculates how many rabbits should die each year. This encapsulates the death equation in the Rabbit 
 * Population model. The actual equation is (population / lifeTime) * deathMultiplier. 
 * 
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2005/08/12 20:04:58 $
 */
public class Deaths extends DefaultDrawableNode implements CustomProbeable, Named {
  
  public float lifeTime = 4f;
  public double deaths = 0;
  public CEquation equation;
  public RabbitPopulation rabbitPop;
  public DeathMultiplier deathMult;

  /**
   * Creates a Deaths object using the specified CEquationFactory. This factory creates a CEquation
   * and schedules that CEquation's assignment and evaluation.
   * 
   * @param factory
   */ 
  public Deaths(CEquationFactory factory) {
    super(new RoundRectNetworkItem(235, 64));
    setNodeLabel("Deaths");
    equation = factory.createEquation(this, "(population / lifeTime) * deathMultiplier", "deaths", 1);
    
  }
  
  /**
   * Initialize this Deaths instance. The RabbitPopulation is queried for the current population when
   * the CEquation encapsulated by this Deaths instance is evaluated.
   * 
   * @param population
   * @param deathMult
   */ 
  public void init(RabbitPopulation population, DeathMultiplier deathMult) {
    rabbitPop = population;
    this.deathMult = deathMult;
    deaths = rabbitPop.getPopulation() / lifeTime;
  }

  public float getLifeTime() {
    return lifeTime;
  }

  public void setLifeTime(float lifeTime) {
    this.lifeTime = lifeTime;
  }
  
  public double getDeathMultiplier() {
    double dm = deathMult.getDeathMultiplier();
    return dm;
  }

  public CEquation getEquation() {
    return equation;
  }

  public void setEquation(CEquation equation) {
    this.equation = equation;
  }

  public double getPopulation() {
    return rabbitPop.getPopulation();
  }
  
  public double getDeaths() {
    return deaths;
  }

  public void setDeaths(double deaths) {
    this.deaths = deaths;
  }
  
   // implements Named interface
  public String getName() {
    return "Deaths";
  }

  // implements CustomProbeable interface
  public String[] getProbedProperties() {
    return new String[]{"equation", "lifeTime"};
  }
}
