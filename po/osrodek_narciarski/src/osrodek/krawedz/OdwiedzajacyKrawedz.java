package osrodek.krawedz;

import osrodek.krawedz.wyciag.Wyciag;

public interface OdwiedzajacyKrawedz<R> {
    public R trasa(Trasa trasa);
    public R wyciag(Wyciag wyciag);
}
