package ru.kodeks.docmanager.model.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
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
class Classifiers {

    /** Глобальные типы документов.*/
    val DOCTYPE = 3

    /** region Виды документов*/

    /** Виды входящих документов.*/
    val INCOMING_DOC_TYPES = 40

    /** Виды исходящих документов.*/
    val OUTGOING_DOC_TYPES = 46

    /** Виды ОРД документов.*/
    val DECREE_TYPE = 30

    /** Виды внутренних документов.*/
    val DIRECTIVETYPE = 34

    /** Виды обращений граждан.*/
    val VIDOBR = 28

    /** Виды договоров.*/
    val CONTRACT_KIND = 127
//        #endregion

//        #region Обращение граждан

    /** Формы обращений граждан.*/
    val FORMOBR = 27

    /** Тематики обращений граждан.*/
    val APPROACH_THEMS = 117

    /** Специализированные тематики обращений граждан.*/
    val APPEAL_THEMES = 97

    /** Типы решений ответов на обращения.*/
    val RTA_RESOLUTION = 111

    /** Объект/Место решения ответов на обращения.*/
    val RTA_LOCATION = 112

    /** Причины решений ответов на обращения.*/
    val RTA_REASON = 113

    // Организация

    /** Виды заявителей.*/
    val APPLICANT_TYPES = 42

    /** Организационно-правовые формы организаций.*/
    val ORG_FORM_TYPE = 17

    /** Виды деятельности организаций.*/
    val ORG_WORK_TYPE = 18

    /** Категории заявителей.*/
    val APP_CATEGORY = 25

    /** Социальные группы населения.*/
    val SOCIAL = 26

//        #region Договор

    /**  Статьи бюджета договоров.*/
    val CONTRACT_BUDGET_ITEMS = 129

    /** Тематики договоров.*/
    val CONTRACT_THEMES = 128

    /** Условия расчёта по договору.*/
    val PAYMENT_TERMS = 133

    /** Программы договора.*/
    val CONTRACT_PROGRAM = 136

    /** Валюта платежа.*/
    val CURRENCY = 135
//        #endregion

    /** Типы связей. Его элементы имеют расширенное поле \b revName, содержащее имя обратной связи.*/
    val LINK_TYPES = 14

    /** Результаты согласования. Его элементы имеют расширенное поле \b resultCode, содержащее числовой код <see cref="ApprovalResults"/>.
    Для операции "Согласовать" элементы словаря нужно фильтровать по значениям \b resultCode = \link ApprovalResults
    ApprovalResults::Approved\endlink (2) или \link ApprovalResults ApprovalResults::ApprovedWithRemarks\endlink (3).*/
    val CRESULT = 38
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
class CorrespondentTypes {

    /** Исполнитель из глобального классификатора*/
    val GLOBAL_OBJECT = 0

    /** Организация из справочника организаций*/
    val ORGANIZATION = 1
}

/** Организация. Справочник организаций не грузится на клиента целиком. Вместо этого на клиента
 * возвращаются все организации, фигурирующие в активных документах.*/
class Organization(
    /** Уникальный идентификатор(Guid)*/
    @SerializedName("uid")
    @Expose
    var uid: String? = null,
    /** Наименование*/
    @SerializedName("name")
    @Expose
    var fullName: String? = null,
    /** Краткое наименование*/
    @SerializedName("shortName")
    @Expose
    var shortName: String? = null,
    /** Вид заявителя. Справочник \link Classifiers Classifiers::ApplicantTypes\endlink.*/
    @SerializedName("orgType")
    @Expose
    var orgType: Int? = null,
    /** Организационно-правовая форма организации. Справочник \link Classifiers Classifiers::ORG_FORM_TYPE\endlink.*/
    @SerializedName("formType")
    @Expose
    var formType: Int? = null,
    /** Вид деятельности организации. Справочник \link Classifiers Classifiers::ORG_WORK_TYPE\endlink.*/
    @SerializedName("workType")
    @Expose
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
    var addresses: List<OrgAddress>? = null,
    /** Категория заявителя. Справочник \link Classifiers Classifiers::APP_CATEGORY\endlink.*/
    @SerializedName("appCategory")
    @Expose
    var appCategory: Int? = null,
    /** Социальная группа населения. Справочник \link Classifiers Classifiers::SOCIAL\endlink.*/
    @SerializedName("social")
    @Expose
    var socialGroup: Int? = null
) : ObjectBase()

/** Типы адресов*/
class AddressType {

    /** Почтовый адрес*/
    val MAIN = 0

    /** Юридический адрес*/
    val JURIDICAL = 1

    /** Рабочий адрес*/
    val WORK = 2

    /** Иной адрес*/
    val OTHER = 3
}

/** Адрес организации, см. <see cref="Organization"/>.*/
class OrgAddress {
    /** Тип адреса, см. <see cref="AddressType"/>.*/
    @SerializedName("t")
    @Expose
    var type: Int? = null

    /** Адрес*/
    @SerializedName("a")
    @Expose
    var address: String? = null
}
