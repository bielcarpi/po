.text
j $global

$global:
	li $t0, 3
	j $main

$main:
$E0:
	bge $t0, 13, $E1
	li $t9, 1
	add $t9, $t8, $t0
	move $t0, $t9
	j $E0
$E1:
	li $v0, 0
