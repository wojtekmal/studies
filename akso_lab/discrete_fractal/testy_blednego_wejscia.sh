if [ $# -eq 0 ]
then
    echo "Poprawne użycie: testy_blednego_wejscia.sh <ścieżka do pliku binarnego testowanego programu>"
fi

for filename in testy_blednego_wejscia/*
do
    echo $filename
    $1 0 < $filename > /dev/null
    
    if [ $? -ne 1 ]
    then
        echo "Program powinien zwrócić 1, a zwrócił $?"
        break
    fi
done

for n in '' ' ' 'a' 'A' '-1' '4294967296' '10000000000000000000'
do
    echo "Testowanie błędnego n = $n."
    echo "" | $1 $n > /dev/null

    if [ $? -ne 1 ]
    then
        echo "Program powinien zwrócić 1, a zwrócił $?"
        break
    fi
done