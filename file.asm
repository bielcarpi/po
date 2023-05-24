.text
j $global

$PRINT_INT:
	li $v0, 1
	syscall
	li $v0, 0
	jr $ra

$global:
	li $t2, 10
	li $t1, 0
	j $main

$main:

	sw $a0, 0($sp)
	sw $a1, 4($sp)
	sw $a2, 8($sp)
	sw $a3, 12($sp)
	sw $ra, 16($sp)
	sw $t6, 20($sp)
	sw $t7, 24($sp)
	sw $t8, 28($sp)
	sw $t9, 32($sp)
	sw $s0, 36($sp)
	sw $s1, 40($sp)
	sw $s2, 44($sp)
	addi $sp, $sp, 48

	jal $hola

	subi $sp, $sp, 48
	lw $a0, 0($sp)
	lw $a1, 4($sp)
	lw $a2, 8($sp)
	lw $a3, 12($sp)
	lw $ra, 16($sp)
	lw $t6, 20($sp)
	lw $t7, 24($sp)
	lw $t8, 28($sp)
	lw $t9, 32($sp)
	lw $s0, 36($sp)
	lw $s1, 40($sp)
	lw $s2, 44($sp)

	li $v0, 10
	syscall

$hola:
	li $t0, 1
	li $t6, 0
$E0:
	bge $t6, $t2, $E1
	add $s0, $t1, $t0
	move $t7, $s0
	move $t1, $t0
	move $t0, $t7
	li $s1, 1
	add $s0, $s1, $t6
	move $t6, $s0
	j $E0
$E1:
	move $a0, $t1
	jal $PRINT_INT
	li $v0, 0
	jr $ra
