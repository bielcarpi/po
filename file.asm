.section .text

.global __start

__start:
	li $t0, 3
	li $t2, 4
	mul $t1, $t2
	move $t3, t0
	li $t4, 3
	li $t6, 4
	mul $t5, $t6
	li $t8, 8
	add $t0, t2
	move $t2, t3
	li $t3, 1
	add $t4, a
	move $t6, t4
	li $t7, 123
	li $t9, 3
	mul $t8, $t9
	li $t0, 8
	add $t1, t5
	move $t4, t7
	li $t5, 1
	add $t6, i
	move $t8, t8
	li $t9, 1
	add $t0, b
	move $t2, t9
	li $t3, 1
	add $t4, b
	move $t6, t10
