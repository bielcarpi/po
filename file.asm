.text
j $global

$multiplicarNumeros:
	li $s1, 1
	sub $s0, $a0, $s1
	move $a0, $s0
$E0:
	blt $a0, 1, $E1
	mul $s0, $a1, $a0
	move $a1, $s0
$E2:
	bne $a0, 0, $E3
$E4:
	bge $a0, 0, $E5
	li $t0, 58
	j $E6
$E5:
	li $t0, 30
$E6:
	li $v0, 1
	jr $ra
	j $E8
$E3:
	bne $a0, 1, $E7
	li $t0, 100
	j $E8
$E7:
	li $s1, 1
	add $s0, $a1, $s1
	move $a1, $s0
$E8:
	li $s1, 1
	add $s0, $a0, $s1
	move $a0, $s0
	j $E9
$E1:
	li $s1, 1
	add $s0, $a0, $s1
	move $a0, $s0
	li $v0, 1
	jr $ra
$E9:
	move $v0, $a1
	jr $ra

$PRINT_INT:
	li $v0, 1
	syscall
	li $v0, 0
	jr $ra

$global:
	li $t0, 0
	j $main

$main:

	sw $a0, 0($sp)
	sw $a1, 4($sp)
	sw $a2, 8($sp)
	sw $a3, 12($sp)
	sw $ra, 16($sp)
	sw $t1, 20($sp)
	sw $t2, 24($sp)
	sw $t3, 28($sp)
	sw $t4, 32($sp)
	sw $t5, 36($sp)
	sw $t6, 40($sp)
	sw $t7, 44($sp)
	sw $t8, 48($sp)
	sw $t9, 52($sp)
	sw $s0, 56($sp)
	sw $s1, 60($sp)
	sw $s2, 64($sp)
	addi $sp, $sp, 68


	li $a0, 8
	li $a1, 0
	jal $multiplicarNumeros


	subi $sp, $sp, 68
	lw $a0, 0($sp)
	lw $a1, 4($sp)
	lw $a2, 8($sp)
	lw $a3, 12($sp)
	lw $ra, 16($sp)
	lw $t1, 20($sp)
	lw $t2, 24($sp)
	lw $t3, 28($sp)
	lw $t4, 32($sp)
	lw $t5, 36($sp)
	lw $t6, 40($sp)
	lw $t7, 44($sp)
	lw $t8, 48($sp)
	lw $t9, 52($sp)
	lw $s0, 56($sp)
	lw $s1, 60($sp)
	lw $s2, 64($sp)

	move $t0, $v0

	sw $a0, 0($sp)
	sw $a1, 4($sp)
	sw $a2, 8($sp)
	sw $a3, 12($sp)
	sw $ra, 16($sp)
	sw $t1, 20($sp)
	sw $t2, 24($sp)
	sw $t3, 28($sp)
	sw $t4, 32($sp)
	sw $t5, 36($sp)
	sw $t6, 40($sp)
	sw $t7, 44($sp)
	sw $t8, 48($sp)
	sw $t9, 52($sp)
	sw $s0, 56($sp)
	sw $s1, 60($sp)
	sw $s2, 64($sp)
	addi $sp, $sp, 68


	move $a0, $t0
	jal $PRINT_INT


	subi $sp, $sp, 68
	lw $a0, 0($sp)
	lw $a1, 4($sp)
	lw $a2, 8($sp)
	lw $a3, 12($sp)
	lw $ra, 16($sp)
	lw $t1, 20($sp)
	lw $t2, 24($sp)
	lw $t3, 28($sp)
	lw $t4, 32($sp)
	lw $t5, 36($sp)
	lw $t6, 40($sp)
	lw $t7, 44($sp)
	lw $t8, 48($sp)
	lw $t9, 52($sp)
	lw $s0, 56($sp)
	lw $s1, 60($sp)
	lw $s2, 64($sp)

	li $t0, 0
$E10:
	bge $t0, 10, $E11
	j $E11
	li $s1, 1
	add $s0, $t0, $s1
	move $t0, $s0
	j $E10
$E11:
	li $v0, 10
	syscall
