.text
j $global

$PRINT_INT:
	li $v0, 1
	syscall
	li $v0, 0
	jr $ra

$global:
	li $t0, 100
	li $t1, 101
	j $main

$main:
$E0:
	ble $t0, $t1, $E1

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


	move $a0, $t0
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

	j $E3
$E1:
	ble $t1, $t0, $E2

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


	move $a0, $t1
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

	j $E3
$E2:

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


	li $a0, 1
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

$E3:
	li $v0, 10
	syscall
