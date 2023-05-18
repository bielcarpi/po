.text
j $global

$PRINT_INT:
	li $v0, 1
	syscall
	li $v0, 0
	jr $ra

$global:
	li $t0, 15
	li $t1, 0
	j $main

$main:
	li $t2, 2
	li $t4, 0
$E0:
	bge $t4, $t0, $E1
	add $t9, $t1, $t2
	move $t3, $t9
	move $t1, $t2
	move $t2, $t3
	li $t8, 1
	add $t9, $t8, $t4
	move $t4, $t9
	j $E0
$E1:
	move $a0, $t2
	jal $PRINT_INT
	li $v0, 10
	syscall
