.text
j $global

$multiplicarNumeros:
	li $s1, 1
	sub $s0, $a0, $s1
	move $a0, $s0
$E0:
	blt $a0, 1, $E1
	mul $s0, $a0, $a1
	move $a0, $s0

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


	move $a0, $a0
	li $a1, 1
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

	j $E2
$E1:
	li $v0, 1
	jr $ra
$E2:
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


	li $a0, 3
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

	li $v0, 10
	syscall
