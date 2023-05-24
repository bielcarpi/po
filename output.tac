multiplicarNumeros:
	num = num - 1
	i = 0
E0:
	if i >= 10 goto E1
	j = 0
E2:
	if j >= 10 goto E3
	s0 = i * j
	b = s0
E4:
	if i != 0 goto E5
	goto E4
	goto E5
	goto E4
E5:
	j = j + 1
	goto E2
E3:
	i = i + 1
	goto E0
E1:
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
	t1 = t1
	t1 = t1
	t1 = t1
	t1 = t1
	t1 = 0
	t1 = 0
	t1 = 4294967296
	t1 = 0
	t1 = 4294967296
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


