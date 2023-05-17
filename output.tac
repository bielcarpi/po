global:
	a = 3

main:
E4:
	if a >= 13 goto E5
	t9 = a + 1
	a = t9
	goto E4
E5:
	if z == 1 goto E7
	if z == 2 goto E8
	goto E9
E7:
	t9 = a + 3
	a = t9
	b = b + 1
	goto E6
E8:
	t9 = a + 3
	d = t9
	b = b + 1
	goto E6
E9:
	t9 = d + 3
	c = t9
E6:
	t9 = a + 3
	c = t9
	ret 0

hola:
	i = 3
	i = 0
E0:
	if i >= 10 goto E1
	t9 = a + 1
	a = t9
	i = i + 1
	goto E0
E1:
E2:
	if p >= 3 goto E3
	z = z + 1
E3:
	ret 0


