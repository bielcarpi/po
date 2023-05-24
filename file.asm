.text
j $global

$PRINT_INT:
	li $v0, 1
	syscall
	li $v0, 0
	jr $ra

$global:
	li $t2, 10
	li $t1, 0
	j $main

$main:
	jal $hola
	li $v0, 10
	syscall

$hola:
	li $t0, 1
	li $t6, 0
$E0:
	bge $t6, $t2, $E1
	add $s0, $t1, $t0
	move $t7, $s0
	move $t1, $t0
	move $t0, $t7
	li $s1, 1
	add $s0, $s1, $t6
	move $t6, $s0
	j $E0
$E1:
	move $a0, $t1
	jal $PRINT_INT
	li $v0, 0
	jr $ra
