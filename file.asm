.text
j $global

$global:
	li $t0, 1000
	li $t1, 0
	j $main

$main:
	li $t2, 1
	li $t3, 0
$E0:
	bge $t3, $t0, $E1
	add $t9, $t1, $t2
	move $t4, $t9
	move $t1, $t2
	move $t2, $t4
	li $t8, 1
	add $t9, $t8, $t3
	move $t3, $t9
	j $E0
$E1:
	li $v0, 10
	syscall
