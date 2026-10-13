print('''CREATE TABLE publikacje (
    ID int PRIMARY KEY,
    Tytuł varchar[50] NOT NULL,
    Rok int NOT NULL,
    Autorzy int NOT NULL,
    Punkty int NOT NULL
);''')

f = open('Publikacje.csv', 'r')
f.readline()

for i in range(1, 390):
    line = f.readline()
    vals = line.split(',')

    if vals[1][0] == '"':
        vals[1] = vals[1][1:-1]

    line = ','.join(vals)

    print(f'INSERT INTO publikacje VALUES ({line});')

print('SELECT * FROM publikacje WHERE Rok = 2020 AND Punkty >= Autorzy * 100 ORDER BY Autorzy;')

print('DROP TABLE publikacje;')