.data
	$z1102: .asciiz "\nThe number introduced multiplied by 2 is: "
	$z1101: .asciiz "Input a number: "
	f: .word 0
	d: .word 0

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
	sw $t8, 20($sp)
	sw $t9, 24($sp)
	sw $s0, 28($sp)
	sw $s1, 32($sp)
	sw $s2, 36($sp)
	addi $sp, $sp, 40


	la $a0, $z1101
	jal $PRINT_STR


	subi $sp, $sp, 40
	lw $a0, 0($sp)
	lw $a1, 4($sp)
	lw $a2, 8($sp)
	lw $a3, 12($sp)
	lw $ra, 16($sp)
	lw $t8, 20($sp)
	lw $t9, 24($sp)
	lw $s0, 28($sp)
	lw $s1, 32($sp)
	lw $s2, 36($sp)


	sw $a0, 0($sp)
	sw $a1, 4($sp)
	sw $a2, 8($sp)
	sw $a3, 12($sp)
	sw $ra, 16($sp)
	sw $t8, 20($sp)
	sw $t9, 24($sp)
	sw $s0, 28($sp)
	sw $s1, 32($sp)
	sw $s2, 36($sp)
	addi $sp, $sp, 40


	jal $READ_INT


	subi $sp, $sp, 40
	lw $a0, 0($sp)
	lw $a1, 4($sp)
	lw $a2, 8($sp)
	lw $a3, 12($sp)
	lw $ra, 16($sp)
	lw $t8, 20($sp)
	lw $t9, 24($sp)
	lw $s0, 28($sp)
	lw $s1, 32($sp)
	lw $s2, 36($sp)

	move $t0, $v0

	sw $a0, 0($sp)
	sw $a1, 4($sp)
	sw $a2, 8($sp)
	sw $a3, 12($sp)
	sw $ra, 16($sp)
	sw $t8, 20($sp)
	sw $t9, 24($sp)
	sw $s0, 28($sp)
	sw $s1, 32($sp)
	sw $s2, 36($sp)
	addi $sp, $sp, 40


	la $a0, $z1102
	jal $PRINT_STR


	subi $sp, $sp, 40
	lw $a0, 0($sp)
	lw $a1, 4($sp)
	lw $a2, 8($sp)
	lw $a3, 12($sp)
	lw $ra, 16($sp)
	lw $t8, 20($sp)
	lw $t9, 24($sp)
	lw $s0, 28($sp)
	lw $s1, 32($sp)
	lw $s2, 36($sp)

	li $s1, 2
	mul $s0, $t0, $s1
	move $t0, $s0

	sw $a0, 0($sp)
	sw $a1, 4($sp)
	sw $a2, 8($sp)
	sw $a3, 12($sp)
	sw $ra, 16($sp)
	sw $t8, 20($sp)
	sw $t9, 24($sp)
	sw $s0, 28($sp)
	sw $s1, 32($sp)
	sw $s2, 36($sp)
	addi $sp, $sp, 40


	move $a0, $t0
	jal $PRINT_INT


	subi $sp, $sp, 40
	lw $a0, 0($sp)
	lw $a1, 4($sp)
	lw $a2, 8($sp)
	lw $a3, 12($sp)
	lw $ra, 16($sp)
	lw $t8, 20($sp)
	lw $t9, 24($sp)
	lw $s0, 28($sp)
	lw $s1, 32($sp)
	lw $s2, 36($sp)

	li $v0, 10
	syscall

$PRINT_STR:
	li $v0, 4
	syscall
	li $v0, 0
	jr $ra
