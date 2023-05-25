.text
j $global

$global:
	move $t0, $t1
	li $t2, 3
	j $main

$main:
	move $t0, $t4

	sw $a0, 0($sp)
	sw $a1, 4($sp)
	sw $a2, 8($sp)
	sw $a3, 12($sp)
	sw $ra, 16($sp)
	sw $t3, 20($sp)
	sw $t4, 24($sp)
	sw $t5, 28($sp)
	sw $t6, 32($sp)
	sw $t7, 36($sp)
	sw $t8, 40($sp)
	sw $t9, 44($sp)
	sw $s0, 48($sp)
	sw $s1, 52($sp)
	sw $s2, 56($sp)
	addi $sp, $sp, 60


	move $a0, $t3
	jal $PRINT_STR


	subi $sp, $sp, 60
	lw $a0, 0($sp)
	lw $a1, 4($sp)
	lw $a2, 8($sp)
	lw $a3, 12($sp)
	lw $ra, 16($sp)
	lw $t3, 20($sp)
	lw $t4, 24($sp)
	lw $t5, 28($sp)
	lw $t6, 32($sp)
	lw $t7, 36($sp)
	lw $t8, 40($sp)
	lw $t9, 44($sp)
	lw $s0, 48($sp)
	lw $s1, 52($sp)
	lw $s2, 56($sp)

	li $v0, 10
	syscall

$PRINT_STR:
	li $v0, 4
	syscall
	li $v0, 0
	jr $ra
