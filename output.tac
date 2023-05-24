multiplicarNumeros:
	num = num - 1
E0:
	if num < 1 goto E1
	s0 = b * num
	b = s0
E2:
	if num != 0 goto E3
	ret 1
	goto E4
E3:
	b = b + 1
E4:
	num = num + 1
	goto E5
E1:
	num = num + 1
	ret 1
E5:
	ret b

PRINT_INT:
	syscall 1
	ret 0

global:
	a = 0

main:
	savec
	addp 0 8
	addp 1 0
	call multiplicarNumeros
	loadc
	a = v0
	savec
	addp 0 a
	call print
	loadc
	a = 0
E6:
	if a >= 10 goto E7
	goto E7
	a = a + 1
	goto E6
E7:
	ret 0


