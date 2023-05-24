multiplicarNumeros:
	num = num - 1
E0:
	if num < 1 goto E1
	s0 = num * b
	num = s0
	savec
	addp 0 num
	addp 1 1
	call multiplicarNumeros
	loadc
	goto E2
E1:
	ret 1
E2:
	ret 0

global:
	c = 0

main:
	savec
	addp 0 3
	addp 1 0
	call multiplicarNumeros
	loadc
	ret 0


