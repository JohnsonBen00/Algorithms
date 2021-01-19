# PA5-GraphAlgPerf
Starter code for PA5.  See the PA5 writeup in the Writeups repository for the github assignment link.

See https://github.com/UACS210Fall2018/PA-Drill-Section-Writeups/tree/master/PA5-GraphAlgPerf

heuristic: cost = 1.7976931348623157E308, 0 milliseconds
mine: cost = 1.7976931348623157E308, 0 milliseconds
backtrack: cost = 183.86, 0 milliseconds

The performance between my heuristic method and mine method run about the same time,
however, my mine method gets a lower cost than both enumerate and heuristic methods.
I believe it's because I loop through the cities left after finding the closest cities
in each iteration -- making less iterations each time and finding the best route.