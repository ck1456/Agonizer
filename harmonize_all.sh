#!/bin/bash

datadir="data/"
solndir="soln/"
invoke="java -jar harmonizer.jar"
rm -rf "${solndir}"

#Problem 1
for n in 1 2 3 4 5
do
  infile="${datadir}problem_${n}.in"
  outfile="${solndir}problem_${n}.out"
  echo "${infile}"
  ${invoke} ${infile} ${outfile}
  
  java -jar agonizer.jar "${infile}" "${outfile}"
done
echo

