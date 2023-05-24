multiplicarNumeros:
	num = num - 1
E0:
	if num < 1 goto E1
	savec
	addp 0 num
	addp 1 0
	call multiplicarNumeros
	loadc
	b = v0
	s0 = b * num
	b = s0
	goto E2
E1:
	ret 1
E2:
	ret b

PRINT_INT:
	syscall 1
	ret 0

global:
	a = 0

main:
	savec
	addp 0 3
	addp 1 0
	call multiplicarNumeros
	loadc
	a = v0
	savec
	addp 0 a
	call print
	loadc
	ret 0


