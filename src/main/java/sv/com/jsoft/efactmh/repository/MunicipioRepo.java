package sv.com.jsoft.efactmh.repository;

import sv.com.jsoft.efactmh.model.Municipio;

/**
 *
 * @author migue
 */
public class MunicipioRepo extends AbstractRepository<Municipio, Integer> {

    public MunicipioRepo() {
        super(Municipio.class);
    }
}
