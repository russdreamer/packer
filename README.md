# packer

There are 2 general steps in this API implementation: <br>
- parsing text document
- algorithm to find optimal items to fit

Parsing is based on regex and getting by groups. It also provides validation function.<br>

### Algorithm choosing
Firstly we need an accurate algorithm only. <br>
There are 2 general ways:<br>
- brute force algorithm. The complexity is 2^n which is awful
- dynamic algorithm. Complexity is n * w. Where w is a weight limit. Let's say it's about polynomial

### Why I've chosen brute force algorithm despite it seems so bad:
Dynamic algorithm is suitable for integer weights because it build an array for every possible weight. 
We can not build array with float indexes. What we can do is multiply float weights to make it integer.<br>
Fisrt we had maximal 15 items and weight 100. That means our angorith do n * 100 iteration constantly. 1500 maximum.<br>
But if we want to multiply - it increase up to 150000 iteration. And it's only we're sure there are not more than 2 digits after a dot sign.<br>
<br>
Brute force algorithm provides 2^n iteration. Maximal 2^15 = ~32768 iteration. It's almost 5 times faster. And it doesn't depend if we use integer or float.
And finaly suppose we have 15 item which is max. In fact amount of iteration can be from n to 2^n iteration. If all our items are bigger than weight limit - it's only 15 iterations.<br>
However if we use dynamic algorithm it's constantly 150000 iterations, which is 10000 times slower. 
Same situation about taking memory for both algorithms.<br> 
But from 18 items brute force algorithm makes sence. It starts to work much slower. However we have 15 items max constraint.

### Data structures
I use ArrayList for items and LinkedList for everything else. Because I get access by index to all the item elements as a main operation.
To do it by O(1) I use ArrayList. I avoid to use poor array as I do reducing and iterate operation as well.
All others list I use as LinkedList because I add to beginning and ending as a main operation.<br>
I use stream API for small operations as this structure iterates quite slow.

### Float or Integer
As I mentioned we could use integer weights with dynamic algorithm, but it would increase iterations 100 times.
Also we can multiply weight for brute force algorithm because it doesn't increase iteration for this algorithm. 
If we would use float number for weight limit - it can produce mistakes as float/double isn't accurate with calculation and comparasion. However it's only integer weight limits in our cases and float number works well here.
