.data
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

$global:
	li $t0, 0
	li $t4, 1
	la $t9, d
	lw $t9, 0($t9)
	li $t9, 2
	la $t8, d
	sw $t9, 0($t8)
	li $t3, 3
	la $t9, f
	lw $t9, 0($t9)
	li $t9, 4
	la $t8, f
	sw $t9, 0($t8)
	li $t1, 5
	li $t5, 6
	li $t6, 7
	li $t2, 8
	li $t7, 9
	j $main

$main:
	la $t8, d
	lw $t8, 0($t8)
	mul $s0, $t4, $t8
	mul $s0, $s0, $t3
	la $t8, f
	lw $t8, 0($t8)
	mul $s0, $s0, $t8
	mul $s0, $s0, $t1
	mul $s0, $s0, $t5
	mul $s0, $s0, $t6
	mul $s0, $s0, $t2
	mul $s0, $s0, $t7
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
