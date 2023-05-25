.data
	$z1101: .asciiz "Invalid option"

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
	li $t0, 5
	li $t1, 2
	li $t2, 2
	j $main

$main:
	beq $t2, 0, $E1
	beq $t2, 1, $E2
	beq $t2, 2, $E3
	j $E4
$E1:

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


	move $a0, $t1
	jal $PRINT_INT


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

	j $E0
$E2:

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


	move $a0, $t0
	jal $PRINT_INT


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

	j $E0
$E3:
	add $s0, $t0, $t1
	move $t3, $s0

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
	jal $PRINT_INT


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

	j $E0
$E4:
$E0:
	li $v0, 10
	syscall
