#!/bin/bash

datadir="data/"
invoke="java -jar Agonizer.jar"

#Problem 1
for n in 1 2 3 4 5
do
  infile="${datadir}problem_${n}.in"
  outfile="${datadir}problem_${n}.out"
  echo "${infile}"
  ${invoke} ${infile} ${outfile}
done
echo

