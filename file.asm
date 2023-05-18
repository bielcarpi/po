
$global:
li $t9, $t0
move a, $t9

$main:

$E4:
null
li $t9, 1
add $t9, $t8, $t0
move $t9, t9
li $t9, $t0
move a, $t9
j $E4


$E5:
null
null
j $E9


$E7:
li $t9, 3
add $t9, $t8, $t0
move $t9, t9
li $t9, $t0
move a, $t9
li $t9, 1
add $t9, $t8, $t0
move $t9, $t0
j $E6


$E8:
li $t9, 3
mul $t9, $t8, $t0
move $t9, t9
li $t9, $t0
move a, $t9
li $t9, 1
add $t9, $t8, $t0
move $t9, $t0
j $E6


$E9:
li $t8, 9
li $t7, 3
add $t9, $t8, $t7
move $t9, t9
li $t9, $t0
move a, $t9

$E6:
li $t9, 3
add $t9, $t8, $t0
move $t9, t9
li $t9, $t0
move a, $t9
null

$hola:
li $t9, $t0
move i, $t9
li $t9, $t0
move i, $t9

$E0:
null
li $t9, 1
add $t9, $t8, $t0
move $t9, t9
li $t9, $t0
move a, $t9
li $t9, 1
add $t9, $t8, $t0
move $t9, $t0
j $E0


$E1:
and $t9, $t0, $t0
move $t9, t9
li $t9, $t0
move a, $t9

$E2:
null
li $t9, 1
add $t9, $t8, $t0
move $t9, $t0

$E3:
null
