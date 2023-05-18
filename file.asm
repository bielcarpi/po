.text
j $global

$PRINT_INT:

$global:
	li $t0, 10
	li $t1, 0
	j $main

$main:
	li $t2, 1
	li $t3, 0
$E0:
	bge $t3, $t0, $E1
	jal $print
	jal $firstTerm
	add $t9, $t1, $t2
	move $t4, $t9
	li $t8, 1
	add $t9, $t8, $t3
	move $t3, $t9
	j $E0
$E1:
	move $t1, $t2
	move $t2, $t4
