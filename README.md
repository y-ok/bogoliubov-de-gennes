# bogoliubov-de-gennes

## Feature

This program describes the simulation of two-componet ultracold fermions
trapped in a two dimentional optical lattice.
It is enable to simulate a variety of mesurable physical quantities 
by tunning 5 parameters of the following.

- Hopping energy
- Interparticle attractive interaction
- Imbalanced population
- Strength of a trapped potential
- Temparature

Also, the program provide the following distributions in real space 
that can be simulated.

- Up-spin particles or down-spin particles
- Superfluid parameters
- Magnetization

## Model

The following Bogoliubov de Gennes Hamiltonian is determined numerically by diagonalization
within the mean field approximation.

<img src="http://latex.codecogs.com/png.latex?%5Cdpi%7B0%7D%20%5Cbg_white%20%5Cdisplaystyle%7B%0A%5Chat%7B%5Ccal%20H%7D%20%3D%20-t%5Csum_%7B%7B%5Clangle%7Dij%7B%5Crangle%7D%5Csigma%7D%0A%5Cleft(%20%0A%5Chat%7Bc%7D%5E%5Cdagger_%7Bi%5Csigma%7D%0A%5Chat%7Bc%7D_%7Bj%5Csigma%7D%2B%5Crm%7BH.c.%7D%0A%5Cright)%0A%2B%5Csum_%7Bi%5Csigma%7D%0A%5Cleft(%0AV_%7Bi%7D-%0A%5Cmu_%7B%5Csigma%7D%0A%5Cright)%5Chat%7Bn%7D_%7Bi%5Csigma%7D%0A%2B%5Csum_%7Bi%7DU%0A%5Chat%7Bn%7D_%7Bi%5Cuparrow%7D%5E%5Cdagger%0A%5Chat%7Bn%7D_%7Bi%5Cdownarrow%7D%0A%7D">
