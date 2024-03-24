package Dao;

import model.Supplier;

public interface SupplierDao {
    Supplier getSupplierByLibraryAndType(String libraryName, String suppliesType);
}
