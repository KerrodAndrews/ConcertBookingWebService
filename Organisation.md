## What each team member did

Nik Naidu (UPI - nnai148, GitHub - n1kz1la) domain model and DTO annotations, worked on resource methods, worked on finalising the project and making sure everything worked together, communicated and helped regularly with the team.

Rohaan Patel-Kumar (UPI - rpat208, GitHub - rpat208) implemented the ConcertResource class, implemented necessary mappers, updated the paths for resource methods so they are linked correctly, and communicated and helped regularly with the team.

Kerrod Andrews (UPI - kand684, GitHub - KerrodAndrews) designed and implemented the domain model, created Organisation.md, team leader, communicated and helped regularly with the team.

In total, all members participated equally, and we are very happy with each other's workload and how we worked together as a team.

## Strategy used to minimise the chance of concurrency errors in program execution

To minimise the chance of concurrency errors in program execution, such as seats being double booked, we used @Version annotations in JPA entities such as User, Subscription and Reservation. This detects concurrent modifications to the same datastore record, 
and prevents conflicting attempts to modify the same record.

## How the domain model was organised

The domain model contains 7 classes: Concert, ConcertPerformer, Performer, Reservation, Seat, Subscription, and User, as well as a subclass SeatId in Seat. All classes are represented with JPA entities. Relationships between
entities are described using annotations such as @ManyToMany and @ManyToOne.
