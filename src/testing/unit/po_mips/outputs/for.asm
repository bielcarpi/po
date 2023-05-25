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
	li $t1, 0
	j $main

$main:
	li $t6, 0
	li $t6, 0
$E6:
	bge $t6, 10, $E7
	li $s1, 2
	add $s0, $t2, $s1
	move $t2, $s0
	li $s1, 1
	add $s0, $t6, $s1
	move $t6, $s0
	j $E6
$E7:

	sw $a0, 0($sp)
	sw $a1, 4($sp)
	sw $a2, 8($sp)
	sw $a3, 12($sp)
	sw $ra, 16($sp)
	sw $t2, 20($sp)
	sw $t3, 24($sp)
	sw $t4, 28($sp)
	sw $t5, 32($sp)
	sw $t6, 36($sp)
	sw $t7, 40($sp)
	sw $t8, 44($sp)
	sw $t9, 48($sp)
	sw $s0, 52($sp)
	sw $s1, 56($sp)
	sw $s2, 60($sp)
	addi $sp, $sp, 64


	move $a0, $t2
	jal $PRINT_INT


	subi $sp, $sp, 64
	lw $a0, 0($sp)
	lw $a1, 4($sp)
	lw $a2, 8($sp)
	lw $a3, 12($sp)
	lw $ra, 16($sp)
	lw $t2, 20($sp)
	lw $t3, 24($sp)
	lw $t4, 28($sp)
	lw $t5, 32($sp)
	lw $t6, 36($sp)
	lw $t7, 40($sp)
	lw $t8, 44($sp)
	lw $t9, 48($sp)
	lw $s0, 52($sp)
	lw $s1, 56($sp)
	lw $s2, 60($sp)

	li $v0, 10
	syscall
