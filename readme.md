<p align="center" style="margin-top: 50px">
  <img src="resources/logo.png" alt="Logo PO" width="500">
</p>

<h1 align="center">The PO Language Compiler</h1>

<p align="center">
  <a href="https://www.java.com">
    <img src="https://img.shields.io/badge/Java-17-red.svg">
  </a>
  <a href="https://github.com/bielcarpi">
    <img src="https://img.shields.io/badge/Development Stage-blue.svg">
  </a>
    <a href="https://opensource.org/licenses/BSD-3-Clause">
    <img src="https://img.shields.io/badge/Open%20Source-%E2%9D%A4-brightgreen.svg">
  </a>

</p>
<p align="center">
PO is a simple and intuitive programming language, fundamentally oriented towards the world of teaching. What is intended with this type of language is to achieve that it can be used as a first contact with the world of programming. The creators of the language want future programmers to be able to use it as a base language with which they can understand how the world of programming works. In this way, show a simple programming method and get more and more people encouraged to program and can participate in the community of software developers.
</p>

## PO Features
- [x] Untyped and Dynamic (but compiled!) Language
- [x] Variables and Constants
- [x] Comments (Single '/' and Multiline '/* */')
- [x] Functions (fun)
- [x] If, Switch, While, For, Foreach support
- [x] Recursion
- [x] Compiles to MIPS Architecture
- [x] Optimized for MIPS Architecture
- [ ] Support for other Architectures

## User Manual & Code Examples
### Operations and Expressions
```javascript
func calculator(num1, num2, op) {
    var result = 0

    if op == 0 {
        result = num1 + num2
    }
    elsif op == 1 {
        result = num1 - num2
    }
    elsif op == 2 {
        if num1 <= 65000 and num2 <= 65000 {
            result = num1 * num2
        }
        else {
            print("The result can be bigger than 32 bits")
        }
    }
    elsif op == 3 {
        result = num1 / num2
    }
    else {
        if op == 4 {
            print("Mod is not supported yet")
        }
        else {
            print("OP should be between 0 and 3")
        }
    }

    ret result
}

main() {
    var num1 = 100
    var num2 = 100
    var op = 0 // 0 = sum - 1 = sub - 2 = mult - 3 = div
    var times = 0 // How many times the op will be done

    var result = calculator(num1, num2, op, times)
    print("The result is: \n")
    print(result)
    print("\n")
```

### Recursive Factorial
```javascript
func recFactorial(num) {
    var aux

    if num >= 1 {
        aux = recFactorial(num - 1)
        ret num * aux
    }
    else {
        ret 1
    }
}

main() {
    prints(" *** Factorial Calculator *** \n")
    prints("Enter a number: ")
    var num = read()
    if num <= 12 {
        prints("Calculating the factorial of ")
        print(num)
        var factorial = recFactorial(num)
        prints("\nThe factorial result is: ")
        print(factorial)
    }
    else {
        prints("The number is too big, please enter a number between 0 and 12")
    }
}
```

### Simple Sorting Algorithm
```javascript
var v1 = 5
var v2 = 2
var v3 = 15
var v4 = 50000
var v5 = 1000

func sortVars(n) {
    var i = 0
    var j = 0
    var aux = 0

    for i = 0, i < n, i++ {
        for j = 0, j < n, j++ {
            if j == 0 and v1 > v2 {
                aux = v1
                v1 = v2
                v2 = aux
            }
            if j == 1 and v2 > v3 {
                aux = v2
                v2 = v3
                v3 = aux

            }
            if j == 2 {
               if v3 > v4 {
                    aux = v3
                    v3 = v4
                    v4 = aux
                }
            }
            if j == 3 {
                if v4 > v5 {
                    aux = v4
                    v4 = v5
                    v5 = aux
                }
            }
        }
    }
}

main() {
    var n = 5 //Num of variables
    prints("Sorting the 5 variables [v1, v5]\n")
    sortVars(n)
    prints("    **********\n\t")
    print(v1)
    prints("\t")
    print(v2)
    prints("\t")
    print(v3)
    prints("\t")
    print(v4)
    prints("\t")
    print(v5)
    prints("    **********\n")
}
```

## How To Use the Compiler
```bash
# Update the Main.java file with the path to the PO file you want to compile
private final static String file = "src/testing/integration/po_mips/inputs/recursive-factorial.po";

# Run the Main.java file

# The MIPS code will be generated in the same folder as the PO file, with the same name and extension ".asm"
# Also, an intermediate TAC file will be generated, named "file.tac", in the root folder of the project
```

## Modify the Language Grammar
As the language is defined using a grammar.txt file, you can modify it to add new features to the language, or change it as you like. 
The compiler itself takes care of parsing this grammar and generating the corresponding Parsing Table (recursively finding first, follows...).

The grammar is LL(1), defined using [Backus-Naur](https://en.wikipedia.org/wiki/Backus–Naur_form) form.
You can find "grammar.txt" in the root folder of the project.


## Authors
Marc Geremias (marc.geremias@students.salle.url.edu)
<br>
Angel Garcia (angel.garcia@students.salle.url.edu)
<br>
Guillem Godoy (guillem.godoy@students.salle.url.edu)
<br>
Armand Daussà (armand.daussai@students.salle.url.edu)
<br>
Biel Carpi (biel.carpi@students.salle.url.edu)