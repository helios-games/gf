package gf.model.roulette

import gf.model.core.{Money, MonteCarloSimulator}

import scala.util.Random

object RouletteSimulation extends MonteCarloSimulator[Roulette](
  wallet => new Roulette(Roulette.randomPocket, wallet),
  roulette => roulette.addBet(NumberBet(Money(1), Pocket(1 + new Random().nextInt(Pocket.MAX - 1)))) spin(),
  expectedReturnToPlayer = 0.973,
  expectedRange = 0.01
)