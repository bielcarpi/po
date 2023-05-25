.data
	$z1101: .asciiz "Hello World"

.text
j $global

$global:
	j $main

$main:

	sw $a0, 0($sp)
	sw $a1, 4($sp)
	sw $a2, 8($sp)
	sw $a3, 12($sp)
	sw $ra, 16($sp)
	sw $t0, 20($sp)
	sw $t1, 24($sp)
	sw $t2, 28($sp)
	sw $t3, 32($sp)
	sw $t4, 36($sp)
	sw $t5, 40($sp)
	sw $t6, 44($sp)
	sw $t7, 48($sp)
	sw $t8, 52($sp)
	sw $t9, 56($sp)
	sw $s0, 60($sp)
	sw $s1, 64($sp)
	sw $s2, 68($sp)
	addi $sp, $sp, 72


	la $a0, $z1101
	jal $PRINT_STR


	subi $sp, $sp, 72
	lw $a0, 0($sp)
	lw $a1, 4($sp)
	lw $a2, 8($sp)
	lw $a3, 12($sp)
	lw $ra, 16($sp)
	lw $t0, 20($sp)
	lw $t1, 24($sp)
	lw $t2, 28($sp)
	lw $t3, 32($sp)
	lw $t4, 36($sp)
	lw $t5, 40($sp)
	lw $t6, 44($sp)
	lw $t7, 48($sp)
	lw $t8, 52($sp)
	lw $t9, 56($sp)
	lw $s0, 60($sp)
	lw $s1, 64($sp)
	lw $s2, 68($sp)

	li $v0, 10
	syscall

$PRINT_STR:
	li $v0, 4
	syscall
	li $v0, 0
	jr $ra
