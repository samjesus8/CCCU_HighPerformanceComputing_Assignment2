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