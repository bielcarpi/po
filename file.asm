.data
	$z1102: .asciiz "El numero introduit es: "
	$z1101: .asciiz "Introdueix un numero : "

.text
j $global

$PRINT_INT:
	li $v0, 1
	syscall
	li $a0, 0xA
	li $v0, 11
	syscall
	li $v0, 0
	jr $ra

$READ_INT:
	li $v0, 5
	syscall
	move $v0, $v0
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


	la $a0, $z1101
	jal $PRINT_STR


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


	jal $READ_INT


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


	la $a0, $z1102
	jal $PRINT_STR


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

	li $s1, 2
	mul $s0, $t0, $s1
	move $t0, $s0

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

	li $v0, 10
	syscall

$PRINT_STR:
	li $v0, 4
	syscall
	li $v0, 0
	jr $ra
