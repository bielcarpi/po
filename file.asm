.text
j $global

$PRINT_INT:
	li $v0, 1
	syscall
	li $v0, 0
	jr $ra

$global:
	li $t-1, 10
	li $t-1, 0
	j $main

$main:
	li $t-1, 1
	li $t-1, 0
$E0:
	bge $t-1, $t-1, $E1
	add $t9, $t-1, $t-1
	move $t-1, $t9
	move $t-1, $t-1
	move $t-1, $t-1
	li $t8, 1
	add $t9, $t8, $t-1
	move $t-1, $t9
	j $E0
$E1:
	move $a0, $t-1
	jal $PRINT_INT
	li $v0, 10
	syscall
