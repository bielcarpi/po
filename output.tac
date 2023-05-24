multiplicarNumeros:
	num = num - 1
E0:
	if num < 1 goto E1
	s0 = b * num
	b = s0
E2:
	if num != 0 goto E3
E4:
	if num >= 0 goto E5
	a = 58
	goto E6
E5:
	a = 30
E6:
	ret 1
	goto E8
E3:
	if num != 1 goto E7
	a = 100
	goto E8
E7:
	b = b + 1
E8:
	num = num + 1
	goto E9
E1:
	num = num + 1
	ret 1
E9:
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
E10:
	if a >= 10 goto E11
	goto E11
	a = a + 1
	goto E10
E11:
	ret 0


