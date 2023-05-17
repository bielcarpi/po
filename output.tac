global:
	a = 3

main:
	if z == 1 goto E1
	if z == 2 goto E2
	goto E3
E1:
	t9 = a + 3
	a = t9
	b = b + 1
	goto E0
E2:
	t9 = a + 3
	d = t9
	b = b + 1
	goto E0
E3:
	t9 = d + 3
	c = t9
E0:
	t9 = a + 3
	c = t9
	ret 0


