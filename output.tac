PRINT_INT:
	syscall 1
	a0 = 0xA
	syscall 11
	ret 0

global:
	b = 0
	c = 1
	d = 2
	e = 3
	f = 4
	g = 5
	h = 6
	i = 7
	j = 8
	z = 9

main:
	s0 = c * d
	s0 = s0 * e
	s0 = s0 * f
	s0 = s0 * g
	s0 = s0 * h
	s0 = s0 * i
	s0 = s0 * j
	s0 = s0 * z
	b = s0
	savec
	addp 0 b
	call print
	loadc
	ret 0


