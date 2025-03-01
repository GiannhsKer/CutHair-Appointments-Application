CREATE TABLE public.appointment (
	id varchar(255) NOT NULL,
	dateTime date NOT NULL,
	"name" varchar(255) NOT NULL,
	phone_number varchar(255) NOT NULL,
	CONSTRAINT appointment_pkey PRIMARY KEY (id)
);