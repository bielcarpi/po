
$for:
li $t9, $t0
move i, $t9

$E0:
null
li $t9, 1
add $t9, $t8, $t0
move $t9, $t0
li $t9, 1
add $t9, $t8, $t0
move $t9, $t0
j $E0


$E1:
null

$global:
li $t9, $t0
move a, $t9
li $t9, $t0
move b, $t9
