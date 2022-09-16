package gr.apt.lms.service

interface CrudService<T> {

    fun create(dto: T): Boolean

    fun update(dto: T): Boolean

    fun delete(dto: T): Boolean

}