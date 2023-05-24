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
	b = 0
	t1 = 0
	t2 = 0
	savec
	addp 0 8
	addp 1 0
	call multiplicarNumeros
	loadc
	a = v0
	s0 = 0 + t2
	t1 = s0
	s0 = t2 + 0
	t2 = s0
	s0 = 0 - t2
	t1 = s0
	s0 = t2 - 0
	t2 = s0
	s0 = 0 * t2
	t1 = s0
	s0 = t2 * 0
	t2 = s0
	s0 = 0 / t2
	t1 = s0
	s0 = t2 / 0
	t2 = s0
	s0 = 0 * t2
	t1 = s0
	s0 = t2 * 0
	t2 = s0
	s0 = 0 / t2
	t1 = s0
	savec
	addp 0 a
	call print
	loadc
	a = 0
E3:
	if a >= 10 goto E4
	goto E4
	a = a + 1
	goto E3
E4:
	ret 0


