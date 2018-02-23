Assignment given by Adhoc.com
================================================

#Calculate second lowest cost silver plan (SLCSP)

Problem
-------

You have been asked to determine the second lowest cost silver plan (SLCSP) for
a group of ZIP Codes.

Task
----

You have been given a CSV file, `slcsp.csv`, which contains the ZIP Codes in the
first column. Fill in the second column with the rate (see below) of the
corresponding SLCSP. Your answer is the modified CSV file, plus any source code
used.

Write your code in your best programming language.

The order of the rows in your answer file must stay the same as how they
appeared in the original `slcsp.csv`.

It may not be possible to determine a SLCSP for every ZIP Code given. Check for cases
where a definitive answer cannot be found and leave those cells blank in the output CSV (no
quotes or zeroes or other text).

Additional information
----------------------

The SLCSP is the so-called "benchmark" health plan in a particular area. It is
used to compute the tax credit that qualifying individuals and families receive
on the marketplace. It is the second lowest rate for a silver plan in the rate area.

For example, if a rate area had silver plans with rates of `[197.3, 197.3,
201.1, 305.4, 306.7, 411.24]`, the SLCSP for that rate area would be `201.1`,
since it is the second lowest rate in that rate area.

A plan has a "metal level", which can be either Bronze, Silver, Gold, Platinum,
or Catastrophic. The metal level is indicative of the level of coverage the plan
provides.

A plan has a "rate", which is the amount that a consumer pays as a monthly
premium, in dollars.

A plan has a "rate area", which is a geographic region in a state that
determines the plan's rate. A rate area is a tuple of a state and a number, for
example, NY 1, IL 14.

There are two additional CSV files in this directory besides `slcsp.csv`:

  * `plans.csv` -- all the health plans in the U.S. on the marketplace
  * `zips.csv` -- a mapping of ZIP Code to county/counties & rate area(s)

A ZIP Code can potentially be in more than one county. If the county can not be
determined definitively by the ZIP Code, it may still be possible to determine
the rate area for that ZIP Code.

A ZIP Code can also be in more than one rate area. In that case, the answer is ambiguous
and should be left blank.

We will want to compile your code from source and run it, so please include the
complete instructions for doing so in a COMMENTS file.

##Data samples

`plans.csv`
```
plan_id,state,metal_level,rate,rate_area
74449NR9870320,GA,Silver,298.62,7
26325VH2723968,FL,Silver,421.43,60
09846WB8636633,IL,Gold,361.69,5
78590JQ3204809,SC,Gold,379.97,2
21163YD2193896,AR,Gold,359.75,2
36956VI7724021,TX,Silver,310.12,11
39287DQ4265604,FL,Platinum,420.79,18
```
`zips.csv`
```
zipcode,state,county_code,name,rate_area
36749,AL,01001,Autauga,11
36703,AL,01001,Autauga,11
36003,AL,01001,Autauga,11
36008,AL,01001,Autauga,11
36006,AL,01001,Autauga,11
36022,AL,01001,Autauga,11
36051,AL,01001,Autauga,11
36091,AL,01001,Autauga,11
```
`slcsp.csv`
```
zipcode,rate
64148,
67118,
40813,
18229,
51012,
79168,
54923,
```