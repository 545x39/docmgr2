package ru.kodeks.docmanager.model.data

import androidx.room.*
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/** Объект глобального каталога.*/
@Entity(tableName = "global_objects", primaryKeys = ["uid"])
data class GlobalObject(
    /** Уникальный идентификатор(Guid)*/
    @SerializedName("uid")
    @Expose
    var uid: String = "",
    /** Уникальный идентификатор родителя (заполняется только при обновлениях, когда родитель в передаваемой структуре данных не соответствует родителю из ГК).(Guid)*/
    @SerializedName("parentUid")
    @Expose
    @ColumnInfo(name = "parent_uid")
    var parentUid: String? = null,
    /** Тип объекта ГК. См. <see cref="GlobalObjectType"/>*/
    @SerializedName("type")
    @Expose
    var type: Int? = null,
    /** Наименование подразделения*/
    @SerializedName("subdivision")
    @Expose
    var subdivision: String? = null,
    /** Имя субъекта*/
    @SerializedName("firstName")
    @ColumnInfo(name = "first_name")
    @Expose
    var firstName: String? = null,
    /** Фамилия субъекта*/
    @SerializedName("surname")
    @Expose
    var surname: String? = null,
    /** Отчество субъекта.*/
    @SerializedName("patronymic")
    @Expose
    var patronymic: String? = null,
    /** Текущая должность субъекта*/
    @SerializedName("post")
    @Expose
    var post: String? = null,
    /** Признак руководителя*/
    @SerializedName("leader")
    @Expose
    var leader: Boolean? = null,
    /** Телефон субъекта, подразделения*/
    @SerializedName("phone")
    @Expose
    var phone: String? = null,
    /** Факс*/
    @SerializedName("fax")
    @Expose
    var fax: String? = null,
    /** Адрес электронной почты субъекта/подразделения*/
    @SerializedName("email")
    @Expose
    var email: String? = null,
    /** Адрес, местонахождение объекта, субъекта*/
    @SerializedName("address")
    @Expose
    var address: String? = null,
    /** Фото (в Base64). Есть в базе сервера, на клиент не присылается. */
    @SerializedName("photo")
    @Expose
    @Ignore
//    var byte[] Photo
    var photo: String? = null,
    /** Номер мобильного телефона субъекта*/
    @SerializedName("mobPhone")
    @Expose
    @ColumnInfo(name = "mobile_phone")
    var mobilePhone: String? = null,
    /** Ранг*/
    @SerializedName("rank")
    @Expose
    var rank: Int? = null,
    /** Дочерние элементы каталога (если их нет, то коллекция = null)*/
    @SerializedName("children")
    @Expose
    @Ignore
    var children: List<GlobalObject>? = null,
    /** Относится ли узел к своему подразделению (бывший фильтр ГК).*/
    @SerializedName("mySub")
    @Expose
    @ColumnInfo(name = "my_sub")
    var mySub: Boolean? = null
) : ObjectBase()


/** Типы классификаторов (подмножество таблицы \b CL_NAMES). Элемент словаря описан классом <see cref="ClassifierItem"/>.*/
object Classifiers {

    /** Глобальные типы документов.*/
    const val DOCTYPE = 3

    /** region Виды документов*/

    /** Виды входящих документов.*/
    const val INCOMING_DOC_TYPES = 40

    /** Виды исходящих документов.*/
    const val OUTGOING_DOC_TYPES = 46

    /** Виды ОРД документов.*/
    const val DECREE_TYPE = 30

    /** Виды внутренних документов.*/
    const val DIRECTIVETYPE = 34

    /** Виды обращений граждан.*/
    const val VIDOBR = 28

    /** Виды договоров.*/
    const val CONTRACT_KIND = 127

    //Обращение граждан

    /** Формы обращений граждан.*/
    const val FORMOBR = 27

    /** Тематики обращений граждан.*/
    const val APPROACH_THEMS = 117

    /** Специализированные тематики обращений граждан.*/
    const val APPEAL_THEMES = 97

    /** Типы решений ответов на обращения.*/
    const val RTA_RESOLUTION = 111

    /** Объект/Место решения ответов на обращения.*/
    const val RTA_LOCATION = 112

    /** Причины решений ответов на обращения.*/
    const val RTA_REASON = 113

    // Организация

    /** Виды заявителей.*/
    const val APPLICANT_TYPES = 42

    /** Организационно-правовые формы организаций.*/
    const val ORG_FORM_TYPE = 17

    /** Виды деятельности организаций.*/
    const val ORG_WORK_TYPE = 18

    /** Категории заявителей.*/
    const val APP_CATEGORY = 25

    /** Социальные группы населения.*/
    const val SOCIAL = 26
    //Договор
    /**  Статьи бюджета договоров.*/
    const val CONTRACT_BUDGET_ITEMS = 129

    /** Тематики договоров.*/
    const val CONTRACT_THEMES = 128

    /** Условия расчёта по договору.*/
    const val PAYMENT_TERMS = 133

    /** Программы договора.*/
    const val CONTRACT_PROGRAM = 136

    /** Валюта платежа.*/
    const val CURRENCY = 135

    /** Типы связей. Его элементы имеют расширенное поле \b revName, содержащее имя обратной связи.*/
    const val LINK_TYPES = 14

    /** Результаты согласования. Его элементы имеют расширенное поле \b resultCode, содержащее числовой код <see cref="ApprovalResults"/>.
    Для операции "Согласовать" элементы словаря нужно фильтровать по значениям \b resultCode = \link ApprovalResults
    ApprovalResults::Approved\endlink (2) или \link ApprovalResults ApprovalResults::ApprovedWithRemarks\endlink (3).*/
    const val CRESULT = 38
}

/** Справочник.*/
class Classifier(
    /** Идентификатор классификатора*/
    @SerializedName("cid")
    @Expose
    var classifierId: Int? = null,
    /** Справочники (название -> элементы)*/
    @SerializedName("items")
    @Expose
    var items: List<ClassifierItem>? = null
) : ObjectBase()

/** Элемент справочника.*/

/** Элементы с расширенными значениями выглядят так:
<pre><code>
{
"id": 38000,
"n": "Согласовано",
"e": {
"resultCode": 2
}
}
</code></pre>*/
@Entity(tableName = "classifiers")
class ClassifierItem(
    /** Идентификатор элемента*/
    @SerializedName("id")
    @Expose
    @ColumnInfo(name = "id")
    @PrimaryKey
    var id: Int = 0,
    /** Идентификатор род. элемента*/
    @SerializedName("pid")
    @Expose
    @ColumnInfo(name = "parent_id")
    var parentId: Int? = null,
    /** Наименование элемента*/
    @SerializedName("n")
    @Expose
    @ColumnInfo(name = "name")
    var name: String? = null,
    /** Краткое наименование элемента*/
    @SerializedName("sn")
    @Expose
    @ColumnInfo(name = "short_name")
    var shortName: String? = null,
    /** Дополнительные поля элемента справочника (словарь)*/
    @SerializedName("e")
    @Expose
    @Ignore
    var extra: Map<String, Any>? = null
) : ObjectBase()

/** Тип корреспондента (используется в некоторых полях документа для указания того, куда "смотрит" УИД - на ГК или на организацию)*/
object CorrespondentTypes {

    /** Исполнитель из глобального классификатора*/
    const val GLOBAL_OBJECT = 0

    /** Организация из справочника организаций*/
    const val ORGANIZATION = 1
}

/** Организация. Справочник организаций не грузится на клиента целиком. Вместо этого на клиента
 * возвращаются все организации, фигурирующие в активных документах.*/
@Entity(
    tableName = "organizations", primaryKeys = ["uid"]
)
class Organization(
    /** Уникальный идентификатор(Guid)*/
    @SerializedName("uid")
    @Expose
    var uid: String = "",
    /** Наименование*/
    @SerializedName("name")
    @Expose
    var fullName: String? = null,
    /** Краткое наименование*/
    @SerializedName("shortName")
    @Expose
    @ColumnInfo(name = "short_name")
    var shortName: String? = null,
    /** Вид заявителя. Справочник \link Classifiers Classifiers::ApplicantTypes\endlink.*/
    @SerializedName("orgType")
    @Expose
    @ColumnInfo(name = "org_type")
    var orgType: Int? = null,
    /** Организационно-правовая форма организации. Справочник \link Classifiers Classifiers::ORG_FORM_TYPE\endlink.*/
    @SerializedName("formType")
    @Expose
    @ColumnInfo(name = "form_type")
    var formType: Int? = null,
    /** Вид деятельности организации. Справочник \link Classifiers Classifiers::ORG_WORK_TYPE\endlink.*/
    @SerializedName("workType")
    @Expose
    @ColumnInfo(name = "work_type")
    var workType: Int? = null,
    /** Телефон*/
    @SerializedName("phone")
    @Expose
    var phone: String? = null,
    /** Факс*/
    @SerializedName("fax")
    @Expose
    var fax: String? = null,
    /** Адрес электронной почты*/
    @SerializedName("email")
    @Expose
    var email: String? = null,
    /** Список адресов*/
    @SerializedName("addresses")
    @Expose
    @Ignore
    var addresses: List<OrgAddress>? = null,
    /** Категория заявителя. Справочник \link Classifiers Classifiers::APP_CATEGORY\endlink.*/
    @SerializedName("appCategory")
    @Expose
    @ColumnInfo(name = "applicant_category")
    var appCategory: Int? = null,
    /** Социальная группа населения. Справочник \link Classifiers Classifiers::SOCIAL\endlink.*/
    @SerializedName("social")
    @Expose
    var socialGroup: Int? = null
) : ObjectBase()

/** Типы адресов*/
object AddressType {

    /** Почтовый адрес*/
    const val MAIN = 0

    /** Юридический адрес*/
    const val JURIDICAL = 1

    /** Рабочий адрес*/
    const val WORK = 2

    /** Иной адрес*/
    const val OTHER = 3
}

/** Адрес организации, см. <see cref="Organization"/>.*/
@Entity(
    tableName = "organization_addresses",
    foreignKeys = [ForeignKey(
        entity = Organization::class,
        parentColumns = ["uid"],
        childColumns = ["org_uid"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["org_uid"])]
)
class OrgAddress(
    @ColumnInfo(name = "org_uid")
    var orgUid: String = "",
    /** Тип адреса, см. <see cref="AddressType"/>.*/
    @SerializedName("t")
    @Expose
    @ColumnInfo(name = "type")
    var type: Int? = null,
    /** Адрес*/
    @SerializedName("a")
    @Expose
    @ColumnInfo(name = "address")
    var address: String? = null,
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
)
