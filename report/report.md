# Dynamic Intel, wants code!

- Author of Report -> ```Samuel Jesuthas```

## Assignment Details

- Module -> ```MCOMD3HPC – High Performance Computing```
- Deadline -> ```10th January 2024```
- This is Assignment 2 which is 50% of the overall module grade

## Introduction

- This report will be a detailed analysis of the importance of threading in applications. When a program runs multi-threaded, you are spreading out the resources to multiple workhorses, which increases the performance of your program compared to running it on a single thread.
- The program simply generates 2 1000x1000 matrixes with random values, and multiplies them together. It then takes the result of this and multiplies it by another 1000x1000 randomly generated matrix, and then it will do this for a 3rd time to get 3 iterations of 1000x1000 matrix multiplications.
- Throughout this report, we will analyze the importance of threading in this application as it significantly increases the performance when the multiplications are performed. We will also look at thread pool implementations for this program and we will test how stable and scalable our solution is for business use.

## Github Repository

- Although the code will be submitted as a `.zip` file, you can also view the code in the following repository.
- This repo will contain the final solution of the assignment. All the main code will be in the `src` folder.
- https://github.com/samjesus8/HPC-Assignment2

## Task 1 - Simple Implementation (Non-Threaded)

### matrixEngine.java (Matrix Calculation Handler)
- To perform a simple 1000x1000 matrix multiplication, first we need to generate the 2 matrixes to get our first iteration. Bear in mind we will be doing 3 iterations so to simplify the process, I made a class called `matrixEngine` that will handle everything for us.

- The first method `GenerateBaseMatrixes()` will generate our 2 1000x1000 matrixes which we will multiply together.

```java
public matrixResult GenerateBaseMatrixes()
{
    long[][] matrix1 = new long[1000][1000];
    long[][] matrix2 = new long[1000][1000];

    fillMatrix(matrix1);
    fillMatrix(matrix2);

    return new matrixResult(matrix1, matrix2);
}
```
- The reason I am using `long` as the data type for the matrix, is so that I can handle overflow of values. `long` can support values from `-9,223,372,036,854,775,808` up to `9,223,372,036,854,775,807`.

- Because Java doesn't allow methods to return more than 1 data type, I made another class called `matrixResult` which basically stores the 2 matrixes in one object, also known as a tuple.

- The `fillMatrix()` method simply does a `for` loop through each value of the matrix and fills it with a value that is randomly generated from 1-100.

```java
public void fillMatrix(long[][] matrix)
{
    Random random = new Random();

    for (int i = 0; i < matrix.length; i++) {
        for (int j = 0; j < matrix[i].length; j++) {
            matrix[i][j] = random.nextInt(100);
        }
    }
}
```

- The final method in this class is the `multiplyMatrices()` method, which is what we will be using to multiply 2 matrixes together.

```java
public long[][] multiplyMatrices(long[][] matrix1, long[][] matrix2) 
{
    long[][] resultMatrix = new long[1000][1000];
    for (int i = 0; i < matrix1.length; i++) {
        for (int j = 0; j < matrix2[0].length; j++) {
            for (int k = 0; k < matrix2.length; k++) {
                resultMatrix[i][j] += matrix1[i][k] * matrix2[k][j];
            }
        }
    }

    return resultMatrix;
}
```

- `resultMatrix` is an empty 1000x1000 matrix which we will use as the result variable. Everything we do in the multiplication, will be stored in this variable.

- The triple-nested `for` loop in this method is what performs the multiplication. What it does is iterate through each element of the 2 matrixes and perform dot-product matrix multiplication
    - `i` will iterate over every ROW of `matrix1`
    - `j` will iterate over every COLUMN of `matrix2`
    - `k` will iterate over the COLUMNS of `matrix1` and the ROWS of `matrix2`

- As we are iterating over every element of the matrix, we get the product of the current row of `matrix1` and then multiply it by the product of the current column of `matrix2`. We assign the results of this to the corresponding element of the `resultMatrix` matrix

```java
resultMatrix[i][j] += matrix1[i][k] * matrix2[k][j];
```

### App.java (The main program)

- Now we know how the logic works for generating & multiplying 2 matrixes together, we can now execute these functions and get a proper output

- We have to multiply 3 times:
    - Generate 2 matrixes, multiply them together
    - Get the result of this, multiply by a new 1000x1000 matrix
    - Get the result of this 2nd iteration, multiply by a new 1000x1000 matrix

- I have split the application to run either threaded or non threaded. Selecting option 1 will execute the non threaded method, which is called `performMatrixMultiplicationNoThreading()`

```
Please choose an option to run this program 
1. Run with No Threading 
2. Run with Threading 
3. Exit Program
```

- First of all, let's generate our 2 base matrixes

```java
//Initialize the main Matrix generation class
var MatrixEngine = new matrixEngine();

//Execute method to generate 2 basic matrixes
var matrixes = MatrixEngine.GenerateBaseMatrixes();

//Display the 2 starting matrixes which will be multiplied together to give our first iteration
System.out.println("Matrix 1: \n");
//We will only display the first 10x10 portion of the matrix
printMatrixPreview(matrixes.matrix1, 10, 10);

System.out.println("Matrix 2: \n");
printMatrixPreview(matrixes.matrix2, 10, 10);
```

- The `printMatrixPreview()` method will read the matrix and it can output a variable length of the matrix. In this case, I am just outputting the first 10x10 portion of the matrix

- Now we will perform our first multiplication by using the `multiplyMatrices()` method. We will parse in the 2 matrixes we have just generated

```java
//Executing the multiplication method to get our first iteration
var result1 = MatrixEngine.multiplyMatrices(matrixes.matrix1, matrixes.matrix2);
System.out.println("1st Multiplication /w No Threading: \n");
printMatrixPreview(result1, 10, 10);
```

- Since the method returns a `long[][]` it would be best to store this in a variable, which is what `result1` is.
- We can use the `printMatrixPreview()` again to display the output

- Now we have our first iteration, all we have to do is multiply again twice. We take the output of `result1` and multiply it by a brand new 1000x1000 matrix

```java
//Now a 2nd randomly generated 1000x1000 matrix will be created
long[][] secondIterationMatrix = new long[1000][1000];
MatrixEngine.fillMatrix(secondIterationMatrix);

//We will multiply this matrix by the result of the 1st multiplication
var result2 = MatrixEngine.multiplyMatrices(result1, secondIterationMatrix);
System.out.println("2nd Multiplication /w No Threading: \n");
printMatrixPreview(result2, 10, 10);
```

- The variable `secondIterationMatrix` is an empty 1000x1000 matrix. We can then use the `fillMatrix()` method to fill the matrix with random numbers from 1-100

- We then do this a 3rd time, to get our 3rd final multiplication. `result1`, `result2` & `result3` should show an exponential increase in the output

```java
//And we will do this a 3rd time, for our 3rd iteration
long[][] thirdIterationMatrix = new long[1000][1000];
MatrixEngine.fillMatrix(thirdIterationMatrix);

//Multiplying the 3rd matrix by the result of the 2nd multiplication
var result3 = MatrixEngine.multiplyMatrices(result2, thirdIterationMatrix);
System.out.println("3rd Multiplication /w No Threading: \n");
printMatrixPreview(result3, 10, 10);
```

### Output

- When we select the non-threaded option and run our program. This is what 1 run of the program will give us. Bear in mind, each run will be different as we are randomly generating each matrix

```
Please choose an option to run this program 
1. Run with No Threading
2. Run with Threading
3. Exit Program
1
Matrix 1: 

77 72 77 37 20 70 89 76 97 26 
11 20 96 9 34 69 18 19 11 67  
46 4 35 75 37 72 18 7 57 46   
0 4 57 28 75 32 71 19 68 0    
59 21 47 31 72 97 93 94 62 28 
86 35 53 69 53 23 65 56 85 71 
24 21 55 82 64 1 27 52 34 70  
57 1 45 81 4 72 61 52 12 91 
67 6 92 99 62 92 56 36 17 36
78 65 80 84 96 47 86 50 21 26

Matrix 2:

97 18 77 20 2 89 0 87 55 96
56 45 6 13 50 81 23 62 48 18
86 47 50 96 10 65 84 84 54 5
2 44 61 3 78 23 99 35 10 13
72 76 88 97 64 82 22 74 52 83
10 69 34 63 55 64 2 88 24 77
55 36 96 35 81 80 97 17 48 99
36 99 22 12 6 30 75 23 43 22
79 23 73 95 48 9 55 78 42 95
18 87 8 91 22 32 72 28 94 48

1st Multiplication /w No Threading: 

2438278 2407397 2407884 2450358 2360667 2403498 2404338 2439549 2398866 2384679 
2444874 2342864 2317231 2451679 2253806 2397741 2419864 2395526 2349789 2336533
2546332 2462791 2450428 2555911 2444147 2473095 2440317 2509852 2434294 2416211
2469636 2388853 2370037 2510179 2384292 2460073 2442992 2468564 2418658 2407702
2468588 2440028 2407149 2484501 2394677 2507088 2483130 2466677 2443875 2372175
2594568 2504883 2468205 2572789 2436364 2537037 2520979 2538143 2486535 2461803
2545199 2482525 2450981 2578211 2451627 2485614 2540137 2539503 2514825 2454384
2492393 2466459 2398276 2566665 2364650 2511101 2474637 2504690 2451488 2409902
2423782 2423098 2364612 2491191 2335572 2386281 2411930 2450200 2378925 2365695
2501016 2403962 2377685 2479862 2367785 2421339 2392706 2415075 2379316 2407709

2nd Multiplication /w No Threading: 

119930213627 116311641844 118067288332 118276686567 118822955339 115217744024 119969269203 124282954585 121308374235 116639049726 
118792001434 115186002575 116955743908 117094620741 117688368607 114113950988 118829207959 123136623739 120149427239 115563309151
122734601154 118962414064 120850606924 120980800813 121545182266 117878441376 122812899809 127209275107 124157695609 119369905121
121845779963 118057502123 119874535141 120073100709 120618452149 116948937027 121816156601 126198796644 123108451766 118438056857
121785851224 118066084825 119880404471 120014509148 120625099036 116989192490 121800108576 126216622661 123198286559 118437384310
124903508090 121128775744 123001780962 123119596715 123737845832 119969220115 125019160797 129476847921 126337465688 121527012899
125089069307 121286144610 123162944152 123310958170 123926012962 120155691101 125125150944 129648260815 126509139521 121666945634
122134441806 118394524754 120237951350 120400339022 121014280346 117359203147 122203418706 126605041589 123561198294 118809801076
120642643112 116919256780 118707392262 118836922019 119479202732 115798450866 120596156102 124983299326 121975244305 117264003962
120675940541 117001777288 118818999447 118933073675 119541575846 115907327750 120669489916 125057820860 122065975227 117348608182

3rd Multiplication /w No Threading: 

5733310307089114 5937864848696633 6066718775620380 5797053149248647 5874974713974414 5744420098595024 5783336070883115 5822504757057018 5971347716497296 5948974151488327 
5678534066867310 5881160771666277 6008756911382012 5741691411004505 5818872439480115 5689574888261657 5728052153723260 5766877383488189 5914247297359298 5892131263451883     
5865773797458580 6075108895199380 6206952563379390 5931000414079050 6010752420022524 5877205506851805 5917007985845670 5957023581903027 6109365361992550 6086413117917407     
5819735182454523 6027443773737341 6158228911173323 5884504283346347 5963576209865641 5831062057213664 5870582396359969 5910307886648992 6061400388825501 6038655295392969     
5819849763749711 6027583737445089 6158399181914338 5884589777201093 5963715450552545 5831143375187652 5870628424550355 5910402203208567 6061524771483645 6038785615617622     
5970364075955241 6183376014736277 6317607065925450 6036782347701125 6117923296938470 5981984574260040 6022471584702016 6063273720280247 6218293622478876 6194937304880439     
5978577619761476 6191886705031224 6326297656764180 6045047165048780 6126306057733095 5990189077894587 6030727348143224 6071569960221977 6226815125433917 6203431856938799     
5838393321524106 6046727726128516 6177946519166543 5903314152417001 5982631074968228 5849737482571775 5889388732310246 5929187283979779 6080815779092317 6057995967417101     
5763496945789696 5969187100322116 6098687721486060 5827566341087858 5905913077295711 5774710922782001 5813804077429097 5853155020571525 6002796681555762 5980276465981642     
5767102236900405 5972886748136695 6102489827572954 5831230656024208 5909569333212118 5778262383466106 5817411247573257 5856824158043147 6006568282669383 5983995816585170     

Execution time: 10129 milliseconds
```

- You can see the non-threaded method takes so long, in this particular run, it took us 10,129 ms to execute the whole method. This is because a single threaded program executes every line one by one, compared to multi-threading where you can spread out the workload across multiple workers

- You can see the exponential increase in the results, which means the multiplication is working and is correct

## Task 2 - Multi-threaded Implementation

## Task 3 - Thread Pool Implementation

## Task 4 - Testing the Thread Pool